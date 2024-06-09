package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.MultiListResult
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

open class GetPopularMoviesUseCase @Inject constructor(
  private val repository: MediaRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<MoviesRequestApi, List<MediaItem>>(dispatcher) {
  override fun execute(parameters: MoviesRequestApi): Flow<MultiListResult> {
    val favoriteMediaIdsFlow = repository.fetchFavoriteIds()
    val popularMoviesFlow = repository.fetchPopularMovies(parameters)

    return combine(favoriteMediaIdsFlow, popularMoviesFlow) { favorite, popular ->
      when {
        favorite.isSuccess && popular.isSuccess -> {
          Result.success(
            getMediaWithUpdatedFavoriteStatus(
              favoriteIds = favorite.data,
              mediaResult = popular.data,
            )
          )
        }
        popular.isSuccess -> Result.success(popular.data)
//        favorite.isFailure -> Result.failure // TODO Fix this
//        popular is Result.Error -> popular
//        else -> Result.Loading
        else -> {
          Result.failure(Exception("Something went wrong."))
        }
      }
    }
  }
}
