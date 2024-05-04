package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

open class GetFavoriteMoviesUseCase @Inject constructor(
  private val moviesRepository: MoviesRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<MediaItem.Media>>(dispatcher) {
  override fun execute(parameters: Unit): Flow<Result<List<MediaItem.Media>>> {
    val favoriteMovies = moviesRepository.fetchFavoriteMovies()
    val favoriteTVSeries = moviesRepository.fetchFavoriteTVSeries()

    return combineTransform(
      favoriteMovies,
      favoriteTVSeries,
    ) { moviesFlow, tvFlow ->
      if (moviesFlow.isSuccess && tvFlow.isSuccess) {
        emit(Result.success(moviesFlow.data + tvFlow.data))
      } else {
        emit(Result.failure(Exception("Something went wrong.")))
      }
    }
  }
}
