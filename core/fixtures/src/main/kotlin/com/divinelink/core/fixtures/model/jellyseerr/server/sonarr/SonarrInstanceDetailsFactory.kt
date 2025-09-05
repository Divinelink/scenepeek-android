package com.divinelink.core.fixtures.model.jellyseerr.server.sonarr

import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstanceDetails

object SonarrInstanceDetailsFactory {

  val sonarr = SonarrInstanceDetails(
    server = SonarrInstanceFactory.sonarr,
    profiles = InstanceProfileFactory.tv,
    rootFolders = InstanceRootFolderFactory.tvAll,
  )
}
