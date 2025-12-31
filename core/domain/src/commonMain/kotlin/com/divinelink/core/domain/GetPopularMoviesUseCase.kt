package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.MultiListResult
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Int, List<MediaItem>>(dispatcher.default) {
  override fun execute(parameters: Int): Flow<MultiListResult> =
    repository.fetchPopularMovies(parameters)
}
