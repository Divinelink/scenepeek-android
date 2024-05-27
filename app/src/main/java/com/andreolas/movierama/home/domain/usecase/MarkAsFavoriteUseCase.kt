package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import gr.divinelink.core.util.domain.UseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@Suppress("ThrowingExceptionsWithoutMessageOrCause")
open class MarkAsFavoriteUseCase @Inject constructor(
  private val repository: MediaRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<MediaItem.Media, Unit>(dispatcher) {
  override suspend fun execute(parameters: MediaItem.Media) {
    val isFavorite = repository.checkIfMediaIsFavorite(
      id = parameters.id,
      mediaType = parameters.mediaType
    ).data == true

    val result = if (isFavorite) {
      repository.removeFavoriteMedia(
        id = parameters.id,
        mediaType = parameters.mediaType
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
