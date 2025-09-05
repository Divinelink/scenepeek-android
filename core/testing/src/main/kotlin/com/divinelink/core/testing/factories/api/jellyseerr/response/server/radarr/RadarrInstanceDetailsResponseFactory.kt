package com.divinelink.core.testing.factories.api.jellyseerr.response.server.radarr

import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceDetailsResponse
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.InstanceProfileResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.InstanceRootFolderResponseFactory

object RadarrInstanceDetailsResponseFactory {

  val default = RadarrInstanceDetailsResponse(
    server = RadarrInstanceResponseFactory.radarr,
    profiles = InstanceProfileResponseFactory.movie,
    rootFolders = listOf(InstanceRootFolderResponseFactory.movie),
  )
}
