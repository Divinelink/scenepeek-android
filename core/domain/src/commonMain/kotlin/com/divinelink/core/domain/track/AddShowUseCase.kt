package com.divinelink.core.domain.track

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem

class AddShowUseCase(
  private val repository: MediaRepository,
  dispatcher: DispatcherProvider,
) : UseCase<MediaItem.Media, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: MediaItem.Media) {
    repository.saveMediaItem(mediaItem = parameters)
    val status = repository.getUserShowStatus(parameters.id)

    if (status == null) {
      repository.addShow(parameters.id)
    } else {
      repository.deleteShow(parameters.id)
    }
  }
}
