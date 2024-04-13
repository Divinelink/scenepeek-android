package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import com.andreolas.movierama.home.domain.repository.MultiListResult
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

open class GetPopularMoviesUseCase @Inject constructor(
  private val moviesRepository: MoviesRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<PopularRequestApi, List<MediaItem>>(dispatcher) {
  override fun execute(
    parameters: PopularRequestApi,
  ): Flow<MultiListResult> {
    val favoriteMediaIdsFlow = moviesRepository.fetchFavoriteIds()
    val popularMoviesFlow = moviesRepository.fetchPopularMovies(parameters)

    return combine(favoriteMediaIdsFlow, popularMoviesFlow) { favorite, popular ->
      when {
        favorite is Result.Success && popular is Result.Success -> {
          Result.Success(
            getMediaWithUpdatedFavoriteStatus(
              favoriteIds = favorite.data,
              mediaResult = popular.data,
            )
          )
        }

        popular is Result.Success -> Result.Success(popular.data)
        favorite is Result.Error -> favorite
        popular is Result.Error -> popular
        else -> Result.Loading
      }
    }
  }
}
