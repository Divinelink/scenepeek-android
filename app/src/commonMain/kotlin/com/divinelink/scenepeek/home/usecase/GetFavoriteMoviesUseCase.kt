package com.divinelink.scenepeek.home.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.Flow

open class GetFavoriteMoviesUseCase(
  private val repository: MediaRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, List<MediaItem.Media>>(dispatcher.default) {
  override fun execute(parameters: Unit): Flow<Result<List<MediaItem.Media>>> =
    repository.fetchFavorites()
}
