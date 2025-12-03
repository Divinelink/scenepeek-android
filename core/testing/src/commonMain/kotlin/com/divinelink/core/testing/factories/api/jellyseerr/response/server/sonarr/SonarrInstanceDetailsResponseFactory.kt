package com.divinelink.core.testing.factories.api.jellyseerr.response.server.sonarr

import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceDetailsResponse
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.InstanceProfileResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.InstanceRootFolderResponseFactory

object SonarrInstanceDetailsResponseFactory {

  val default = SonarrInstanceDetailsResponse(
    server = SonarrInstanceResponseFactory.sonarr,
    profiles = InstanceProfileResponseFactory.tv,
    rootFolders = InstanceRootFolderResponseFactory.tvAll,
  )
}
