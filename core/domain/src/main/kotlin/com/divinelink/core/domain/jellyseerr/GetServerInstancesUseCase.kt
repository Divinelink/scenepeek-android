package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.model.media.MediaType

class GetServerInstancesUseCase(
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<MediaType, List<ServerInstance>>(dispatcher.default) {

  override suspend fun execute(parameters: MediaType): List<ServerInstance> = when (parameters) {
    MediaType.TV -> repository.getSonarrInstances().getOrThrow()
    MediaType.MOVIE -> repository.getRadarrInstances().getOrThrow()
    else -> error("Invalid media type")
  }
}
