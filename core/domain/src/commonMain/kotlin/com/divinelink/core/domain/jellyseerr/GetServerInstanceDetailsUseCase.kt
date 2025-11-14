package com.divinelink.core.domain.jellyseerr

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails
import com.divinelink.core.model.media.MediaType

class GetServerInstanceDetailsUseCase(
  private val repository: JellyseerrRepository,
  val dispatcher: DispatcherProvider,
) : UseCase<GetServerInstanceDetailsUseCase.Parameters, ServerInstanceDetails>(dispatcher.default) {

  override suspend fun execute(parameters: Parameters): ServerInstanceDetails =
    when (parameters.mediaType) {
      MediaType.TV -> repository.getSonarrInstanceDetails(parameters.serverId).getOrThrow()
      MediaType.MOVIE -> repository.getRadarrInstanceDetails(parameters.serverId).getOrThrow()
      else -> error("Invalid media type")
    }

  data class Parameters(
    val mediaType: MediaType,
    val serverId: Int,
  )
}
