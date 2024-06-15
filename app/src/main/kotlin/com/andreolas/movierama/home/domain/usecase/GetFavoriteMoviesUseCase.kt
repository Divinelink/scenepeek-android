package com.andreolas.movierama.home.domain.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

open class GetFavoriteMoviesUseCase @Inject constructor(
  private val repository: MediaRepository,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<MediaItem.Media>>(dispatcher) {
  override fun execute(parameters: Unit): Flow<Result<List<MediaItem.Media>>> {
    val favoriteMovies = repository.fetchFavoriteMovies()
    val favoriteTVSeries = repository.fetchFavoriteTVSeries()

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
