package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem

@Suppress("ThrowingExceptionsWithoutMessageOrCause")
open class MarkAsFavoriteUseCase(
  private val repository: MediaRepository,
  dispatcher: DispatcherProvider,
) : UseCase<MediaItem.Media, Unit>(dispatcher.io) {
  override suspend fun execute(parameters: MediaItem.Media) {
    val isFavorite = repository.checkIfMediaIsFavorite(
      id = parameters.id,
      mediaType = parameters.mediaType,
    ).data

    val result = if (isFavorite) {
      repository.removeFavoriteMedia(
        id = parameters.id,
        mediaType = parameters.mediaType,
      )
    } else {
      repository.insertFavoriteMedia(parameters)
    }

    return result.data
//    if (result.isSuccess) {
//      result.data
//    } else if (result.isFailure) {
//      throw result.exceptionOrNull()!!
//    }
//      when (result) {
//      isSuccess -> result.data
//      is result -> throw result.exception
//    }
  }
}
