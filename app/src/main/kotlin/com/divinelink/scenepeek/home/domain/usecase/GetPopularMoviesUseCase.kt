package com.divinelink.scenepeek.home.domain.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.MultiListResult
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import kotlinx.coroutines.flow.Flow

open class GetPopularMoviesUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<MoviesRequestApi, List<MediaItem>>(dispatcher.default) {
  override fun execute(parameters: MoviesRequestApi): Flow<MultiListResult> =
    repository.fetchPopularMovies(parameters)
}
