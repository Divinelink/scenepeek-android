package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.jellyseerr.repository.SeerrRepository

data class DeleteMediaParameters(
  val mediaId: Long,
  val deleteFile: Boolean,
)

class DeleteMediaUseCase(
  private val repository: SeerrRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<DeleteMediaParameters, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: DeleteMediaParameters) {
    if (parameters.deleteFile) {
      repository.deleteFile(parameters.mediaId)
    }

    repository.deleteMedia(parameters.mediaId).getOrThrow()
  }
}
