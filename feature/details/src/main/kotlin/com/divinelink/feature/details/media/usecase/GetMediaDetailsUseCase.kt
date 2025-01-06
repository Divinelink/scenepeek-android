package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.InvalidMediaTypeException
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.feature.details.media.ui.MediaDetailsResult
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
  private val mediaRepository: MediaRepository,
  private val preferenceStorage: PreferenceStorage,
  private val fetchAccountMediaDetailsUseCase: FetchAccountMediaDetailsUseCase,
  private val getMenuItemsUseCase: GetDropdownMenuItemsUseCase,
  private val getDetailsActionItemsUseCase: GetDetailsActionItemsUseCase,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<DetailsRequestApi, MediaDetailsResult>(dispatcher.io) {
  override fun execute(parameters: DetailsRequestApi): Flow<Result<MediaDetailsResult>> =
    channelFlow {
      if (parameters == DetailsRequestApi.Unknown) {
        send(Result.failure(MediaDetailsException()))
        return@channelFlow
      }

      val requestApi = when (parameters) {
        is DetailsRequestApi.Movie -> parameters
        is DetailsRequestApi.TV -> parameters
        DetailsRequestApi.Unknown -> throw InvalidMediaTypeException()
      }

      val similarApi = when (parameters) {
        is DetailsRequestApi.Movie -> SimilarRequestApi.Movie(parameters.id)
        is DetailsRequestApi.TV -> SimilarRequestApi.TV(parameters.id)
        DetailsRequestApi.Unknown -> throw InvalidMediaTypeException()
      }

      val isFavorite = mediaRepository.checkIfMediaIsFavorite(
        id = requestApi.id,
        mediaType = requestApi.mediaType,
      )

      val movieRatingSource = preferenceStorage.movieRatingSource.firstOrNull() ?: RatingSource.TMDB
      val tvRatingSource = preferenceStorage.tvRatingSource.firstOrNull() ?: RatingSource.TMDB

      val ratingSource = when (requestApi) {
        is DetailsRequestApi.Movie -> movieRatingSource
        is DetailsRequestApi.TV -> tvRatingSource
        DetailsRequestApi.Unknown -> throw InvalidMediaTypeException()
      }

      launch(dispatcher.io) {
        repository.fetchMovieDetails(requestApi)
          .catch {
            Timber.e(it)
            send(Result.failure(MediaDetailsException()))
          }
          .map { result ->
            val details = result.data

            val updatedDetails = when (ratingSource) {
              RatingSource.TMDB -> details
              RatingSource.IMDB -> fetchIMDbDetails(details)
              RatingSource.TRAKT -> fetchTraktDetails(details)
            }

            Result.success(
              MediaDetailsResult.DetailsSuccess(
                mediaDetails = updatedDetails.copy(
                  isFavorite = isFavorite.getOrNull() ?: false,
                  ratingCount = updatedDetails.ratingCount,
                ),
                ratingSource = ratingSource,
              ),
            )
          }
          .collect { send(it) }
      }

      launch(dispatcher.io) {
        repository.fetchSimilarMovies(similarApi)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.SimilarSuccess(result.data)))
            }
          }
      }

      launch(dispatcher.io) {
        repository.fetchMovieReviews(requestApi)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.ReviewsSuccess(result.data)))
            }
          }
      }

      if (parameters is DetailsRequestApi.TV) {
        launch(dispatcher.io) {
          repository.fetchAggregateCredits(parameters.id.toLong())
            .catch { Timber.e(it) }
            .collect { result ->
              result.onSuccess {
                send(Result.success(MediaDetailsResult.CreditsSuccess(result.data)))
              }
            }
        }
      }

      launch(dispatcher.io) {
        repository.fetchVideos(requestApi)
          .catch { Timber.e(it) }
          .collect { result ->
            val video = if (parameters is DetailsRequestApi.TV) {
              result.data.firstOrNull()
            } else {
              result.data.firstOrNull { it.officialTrailer }
            }
            send(Result.success(MediaDetailsResult.VideosSuccess(video)))
          }
      }

      launch(dispatcher.io) {
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

      launch(dispatcher.io) {
        getMenuItemsUseCase(requestApi.mediaType)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.MenuOptionsSuccess(result.data)))
            }
          }
      }

      launch(dispatcher.io) {
        getDetailsActionItemsUseCase(Unit)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.ActionButtonsSuccess(result.data)))
            }
          }
      }
    }

  private suspend fun fetchIMDbDetails(details: MediaDetails): MediaDetails =
    details.imdbId?.let { id ->
      repository
        .fetchIMDbDetails(id)
        .firstOrNull()
        ?.getOrNull()
        ?.let {
          details.copy(ratingCount = details.ratingCount.updateRating(RatingSource.IMDB, it))
        }
    } ?: details

  private suspend fun fetchTraktDetails(details: MediaDetails): MediaDetails {
    val mediaType = if (details is Movie) MediaType.MOVIE else MediaType.TV

    return details.imdbId?.let { id ->
      repository
        .fetchTraktRating(mediaType = mediaType, imdbId = id)
        .firstOrNull()
        ?.getOrNull()
        ?.let {
          details.copy(ratingCount = details.ratingCount.updateRating(RatingSource.TRAKT, it))
        }
    } ?: details
  }
}
