package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.InvalidMediaTypeException
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.data.details.model.RecommendedException
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
open class GetMediaDetailsUseCase(
  private val repository: DetailsRepository,
  private val jellyseerrRepository: JellyseerrRepository,
  private val mediaRepository: MediaRepository,
  private val preferenceStorage: PreferenceStorage,
  private val fetchAccountMediaDetailsUseCase: FetchAccountMediaDetailsUseCase,
  private val getMenuItemsUseCase: GetDropdownMenuItemsUseCase,
  private val getDetailsActionItemsUseCase: GetDetailsActionItemsUseCase,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MediaRequestApi, MediaDetailsResult>(dispatcher.default) {
  override fun execute(parameters: MediaRequestApi): Flow<Result<MediaDetailsResult>> =
    channelFlow {
      val detailsCompleted = CompletableDeferred<Unit>()

      if (parameters == MediaRequestApi.Unknown) {
        send(Result.failure(MediaDetailsException()))
        return@channelFlow
      }

      val requestApi = when (parameters) {
        is MediaRequestApi.Movie -> parameters
        is MediaRequestApi.TV -> parameters
        MediaRequestApi.Unknown -> throw InvalidMediaTypeException()
      }

      val isFavorite = mediaRepository.checkIfMediaIsFavorite(
        id = requestApi.id,
        mediaType = requestApi.mediaType,
      )

      val movieRatingSource = preferenceStorage.movieRatingSource.firstOrNull() ?: RatingSource.TMDB
      val tvRatingSource = preferenceStorage.tvRatingSource.firstOrNull() ?: RatingSource.TMDB

      val ratingSource = when (requestApi) {
        is MediaRequestApi.Movie -> movieRatingSource
        is MediaRequestApi.TV -> tvRatingSource
        MediaRequestApi.Unknown -> throw InvalidMediaTypeException()
      }

      launch(dispatcher.default) {
        repository.fetchMediaDetails(requestApi)
          .catch {
            Timber.e(it)
            send(Result.failure(MediaDetailsException()))
            detailsCompleted.complete(Unit)
          }
          .map { result ->
            val details = result.data

            launch ratingFetch@{
              val updatedDetails = when (ratingSource) {
                RatingSource.TMDB -> return@ratingFetch
                RatingSource.IMDB -> fetchIMDbDetails(details)
                RatingSource.TRAKT -> fetchTraktDetails(details)
              }

              send(
                Result.success(
                  MediaDetailsResult.RatingSuccess(rating = updatedDetails.ratingCount),
                ),
              )
            }

            Result.success(
              MediaDetailsResult.DetailsSuccess(
                mediaDetails = details.copy(
                  isFavorite = isFavorite.getOrNull() ?: false,
                ),
                ratingSource = ratingSource,
              ),
            )
          }
          .collect {
            send(it)
            detailsCompleted.complete(Unit)
          }
      }

      launch(dispatcher.default) {
        when (parameters) {
          is MediaRequestApi.Movie -> repository.fetchRecommendedMovies(parameters)
            .catch {
              Timber.e(it)
              throw RecommendedException(MovieTab.Recommendations.order)
            }
            .collect { result ->
              result.onSuccess {
                send(
                  Result.success(
                    MediaDetailsResult.RecommendedSuccess(
                      formOrder = MovieTab.Recommendations.order,
                      similar = result.data.list,
                    ),
                  ),
                )
              }
            }
          is MediaRequestApi.TV -> repository.fetchRecommendedTv(parameters)
            .catch {
              Timber.e(it)
              throw RecommendedException(TvTab.Recommendations.order)
            }
            .collect { result ->
              result.onSuccess {
                send(
                  Result.success(
                    MediaDetailsResult.RecommendedSuccess(
                      formOrder = TvTab.Recommendations.order,
                      similar = result.data.list,
                    ),
                  ),
                )
              }
            }
          MediaRequestApi.Unknown -> throw InvalidMediaTypeException()
        }
      }

      launch(dispatcher.default) {
        repository.fetchMediaReviews(requestApi)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(
                Result.success(
                  MediaDetailsResult.ReviewsSuccess(
                    formOrder = if (parameters is MediaRequestApi.Movie) {
                      MovieTab.Reviews.order
                    } else {
                      TvTab.Reviews.order
                    },
                    reviews = result.data,
                  ),
                ),
              )
            }
          }
      }

      if (parameters is MediaRequestApi.TV) {
        launch(dispatcher.default) {
          repository.fetchAggregateCredits(parameters.id.toLong())
            .catch { Timber.e(it) }
            .collect { result ->
              result.onSuccess {
                send(Result.success(MediaDetailsResult.CreditsSuccess(result.data)))
              }
            }
        }
      }

      launch(dispatcher.default) {
        repository.fetchVideos(requestApi)
          .catch { Timber.e(it) }
          .collect { result ->
            val video = if (parameters is MediaRequestApi.TV) {
              result.data.firstOrNull { it.officialTrailer } ?: result.data.firstOrNull()
            } else {
              result.data.firstOrNull { it.officialTrailer }
            }
            send(Result.success(MediaDetailsResult.VideosSuccess(video)))
          }
      }

      launch(dispatcher.default) {
        when (parameters) {
          is MediaRequestApi.Movie -> jellyseerrRepository.getMovieDetails(parameters.movieId)
            .catch { Timber.e(it) }
            .collect { result ->
              result?.status?.let {
                detailsCompleted.await()
                send(Result.success(MediaDetailsResult.JellyseerrDetailsSuccess(result)))
              }
            }
          is MediaRequestApi.TV -> jellyseerrRepository.getTvDetails(parameters.seriesId)
            .catch { Timber.e(it) }
            .collect { result ->
              result?.status?.let {
                detailsCompleted.await()
                send(Result.success(MediaDetailsResult.JellyseerrDetailsSuccess(result)))
              }
            }
          MediaRequestApi.Unknown -> throw InvalidMediaTypeException()
        }
      }

      launch(dispatcher.default) {
        fetchAccountMediaDetailsUseCase(
          MediaDetailsParams(
            id = requestApi.id,
            mediaType = requestApi.mediaType,
          ),
        )
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.AccountDetailsSuccess(result.data)))
            }
          }
      }

      launch(dispatcher.default) {
        getMenuItemsUseCase(requestApi.mediaType)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.MenuOptionsSuccess(result.data)))
            }
          }
      }

      launch(dispatcher.default) {
        getDetailsActionItemsUseCase(Unit)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.ActionButtonsSuccess(result.data)))
            }
          }
      }
    }

  private suspend fun fetchIMDbDetails(details: MediaDetails): MediaDetails {
    if (details.imdbId == null) {
      return details.copy(
        ratingCount = details.ratingCount.updateRating(
          source = RatingSource.IMDB,
          rating = RatingDetails.Unavailable,
        ),
      )
    }

    return details.imdbId?.let { id ->
      repository
        .fetchIMDbDetails(id)
        .catch { emit(Result.failure(it)) }
        .firstOrNull()
        ?.fold(
          onFailure = {
            details.copy(
              ratingCount = details.ratingCount.updateRating(
                source = RatingSource.IMDB,
                rating = RatingDetails.Unavailable,
              ),
            )
          },
          onSuccess = { result ->
            details.copy(
              ratingCount = details.ratingCount.updateRating(
                source = RatingSource.IMDB,
                rating = result ?: RatingDetails.Unavailable,
              ),
            )
          },
        )
    } ?: details
  }

  private suspend fun fetchTraktDetails(details: MediaDetails): MediaDetails {
    val mediaType = if (details is Movie) MediaType.MOVIE else MediaType.TV
    if (details.imdbId == null) {
      return details.copy(
        ratingCount = details.ratingCount.updateRating(
          source = RatingSource.TRAKT,
          rating = RatingDetails.Unavailable,
        ),
      )
    }

    return details.imdbId?.let { id ->
      repository
        .fetchTraktRating(mediaType = mediaType, imdbId = id)
        .catch { emit(Result.failure(it)) }
        .firstOrNull()
        ?.fold(
          onFailure = {
            details.copy(
              ratingCount = details.ratingCount.updateRating(
                RatingSource.TRAKT,
                RatingDetails.Unavailable,
              ),
            )
          },
          onSuccess = { result ->
            details.copy(ratingCount = details.ratingCount.updateRating(RatingSource.TRAKT, result))
          },
        )
    } ?: details
  }
}
