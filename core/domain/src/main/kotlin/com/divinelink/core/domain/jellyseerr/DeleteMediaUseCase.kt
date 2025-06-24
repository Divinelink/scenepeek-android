package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository

data class DeleteMediaParameters(
  val mediaId: Int,
  val deleteFile: Boolean,
)

class DeleteMediaUseCase(
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<DeleteMediaParameters, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: DeleteMediaParameters) {
    if (parameters.deleteFile) {
      repository
        .deleteFile(parameters.mediaId)
        .onFailure {
          repository.deleteMedia(parameters.mediaId)
        }
        .onSuccess {
          repository.deleteMedia(parameters.mediaId)
        }
    } else {
      repository.deleteMedia(parameters.mediaId)
    }
  }
}
