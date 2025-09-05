package com.divinelink.core.fixtures.model.jellyseerr.server.radarr

import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.model.jellyseerr.server.radarr.RadarrInstanceDetails

object RadarrInstanceDetailsFactory {

  val radarr = RadarrInstanceDetails(
    server = RadarrInstanceFactory.radarr,
    profiles = InstanceProfileFactory.movie,
    rootFolders = listOf(InstanceRootFolderFactory.movie),
  )
}
