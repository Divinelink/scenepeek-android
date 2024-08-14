package com.andreolas.movierama.home.domain.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaType

@Deprecated("Use MarkAsFavoriteUseCase instead")
open class RemoveFavoriteUseCase(
  private val repository: MediaRepository,
  dispatcher: DispatcherProvider,
) : UseCase<Int, Unit>(dispatcher.io) {
  override suspend fun execute(parameters: Int) {
    val result = repository.removeFavoriteMedia(parameters, MediaType.MOVIE)

    return result.data
  }
}
