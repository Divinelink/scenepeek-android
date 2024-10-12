package com.andreolas.movierama.home.domain.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform

open class GetFavoriteMoviesUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, List<MediaItem.Media>>(dispatcher.io) {
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
