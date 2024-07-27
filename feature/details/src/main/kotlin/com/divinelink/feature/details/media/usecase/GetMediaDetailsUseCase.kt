package com.divinelink.feature.details.media.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.InvalidMediaTypeException
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.details.model.MediaDetailsParams
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Suppress("LongMethod")
open class GetMediaDetailsUseCase @Inject constructor(
  private val repository: DetailsRepository,
  private val mediaRepository: MediaRepository,
  private val fetchAccountMediaDetailsUseCase: FetchAccountMediaDetailsUseCase,
  private val getMenuItemsUseCase: GetDropdownMenuItemsUseCase,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DetailsRequestApi, MediaDetailsResult>(dispatcher) {
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
        mediaType = MediaType.from(requestApi.endpoint),
      )

      launch(dispatcher) {
        repository.fetchMovieDetails(requestApi)
          .catch {
            Timber.e(it)
            send(Result.failure(MediaDetailsException()))
          }
          .collect { result ->
            send(
              Result.success(
                MediaDetailsResult.DetailsSuccess(
                  result.data.copy(isFavorite = isFavorite.getOrNull() ?: false),
                ),
              ),
            )
          }
      }

      launch(dispatcher) {
        repository.fetchSimilarMovies(similarApi)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.SimilarSuccess(result.data)))
            }
          }
      }

      launch(dispatcher) {
        repository.fetchMovieReviews(requestApi)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.ReviewsSuccess(result.data)))
            }
          }
      }

      if (parameters is DetailsRequestApi.TV) {
        launch(dispatcher) {
          repository.fetchAggregateCredits(parameters.id.toLong())
            .catch { Timber.e(it) }
            .collect { result ->
              result.onSuccess {
                send(Result.success(MediaDetailsResult.CreditsSuccess(result.data)))
              }
            }
        }
      }

      launch(dispatcher) {
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

      launch(dispatcher) {
        fetchAccountMediaDetailsUseCase(
          MediaDetailsParams(
            id = requestApi.id,
            mediaType = MediaType.from(requestApi.endpoint),
          ),
        )
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.AccountDetailsSuccess(result.data)))
            }
          }
      }

      launch(dispatcher) {
        getMenuItemsUseCase(Unit)
          .catch { Timber.e(it) }
          .collect { result ->
            result.onSuccess {
              send(Result.success(MediaDetailsResult.MenuOptionsSuccess(result.data)))
            }
          }
      }
    }
}
