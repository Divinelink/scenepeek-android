package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem

class MarkAsFavoriteUseCase(
  private val repository: MediaRepository,
  dispatcher: DispatcherProvider,
) : UseCase<MediaItem.Media, Boolean>(dispatcher.default) {
  override suspend fun execute(parameters: MediaItem.Media): Boolean {
    val isFavorite = repository.checkIfMediaIsFavorite(
      id = parameters.id,
      mediaType = parameters.mediaType,
    ).data

    if (isFavorite) {
      repository.removeFavoriteMedia(
        id = parameters.id,
        mediaType = parameters.mediaType,
      )
    } else {
      repository.insertFavoriteMedia(parameters)
    }

    return !isFavorite
  }
}
