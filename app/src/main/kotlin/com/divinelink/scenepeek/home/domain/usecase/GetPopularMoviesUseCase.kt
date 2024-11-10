package com.divinelink.scenepeek.home.domain.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.MultiListResult
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

open class GetPopularMoviesUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MoviesRequestApi, List<MediaItem>>(dispatcher.io) {
  override fun execute(parameters: MoviesRequestApi): Flow<MultiListResult> {
    val favoriteMediaIdsFlow = repository.fetchFavoriteIds()
    val popularMoviesFlow = repository.fetchPopularMovies(parameters)

    return combine(favoriteMediaIdsFlow, popularMoviesFlow) { _, popular ->
      val dataWithUpdatedFavoriteStatus = popular.data.map { mediaItem ->
        val checkIfFavorite = repository.checkIfMediaIsFavorite(
          id = mediaItem.id,
          mediaType = mediaItem.mediaType,
        )
        val isFavorite = checkIfFavorite.getOrNull() == true

        when (mediaItem) {
          is MediaItem.Media.Movie -> mediaItem.copy(isFavorite = isFavorite)
          is MediaItem.Media.TV -> mediaItem.copy(isFavorite = isFavorite)
        }
      }

      Result.success(dataWithUpdatedFavoriteStatus)
    }
  }
}
