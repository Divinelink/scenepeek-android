package com.divinelink.core.fixtures.model.jellyseerr.server.sonarr

import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails

object SonarrInstanceDetailsFactory {

  val sonarr = ServerInstanceDetails(
    server = SonarrInstanceFactory.sonarr,
    profiles = InstanceProfileFactory.tv,
    rootFolders = InstanceRootFolderFactory.tvAll,
  )
}
