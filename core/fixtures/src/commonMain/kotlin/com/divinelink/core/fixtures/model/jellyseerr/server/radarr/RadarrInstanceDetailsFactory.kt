package com.divinelink.core.fixtures.model.jellyseerr.server.radarr

import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails

object RadarrInstanceDetailsFactory {

  val radarr = ServerInstanceDetails(
    server = RadarrInstanceFactory.radarr,
    profiles = InstanceProfileFactory.movie,
    rootFolders = listOf(InstanceRootFolderFactory.movie),
  )
}
