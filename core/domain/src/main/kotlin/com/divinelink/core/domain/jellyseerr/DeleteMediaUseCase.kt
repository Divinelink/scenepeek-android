package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository

class DeleteMediaUseCase(
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<Int, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Int) {
    repository
      .deleteFile(parameters)
      .onFailure {
        repository.deleteMedia(parameters)
      }
      .onSuccess {
        repository.deleteMedia(parameters)
      }
  }
}
