package com.divinelink.core.fixtures.model.jellyseerr.server

import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder

object InstanceRootFolderFactory {

  val tv = InstanceRootFolder(
    id = 5,
    freeSpace = "1.78 TB",
    path = "/data/tv",
  )

  val anime = InstanceRootFolder(
    id = 6,
    freeSpace = "1.78 TB",
    path = "/data/anime",
  )

  val movie = InstanceRootFolder(
    id = 1,
    freeSpace = "1.78 TB",
    path = "/data/movies",
  )

  val tvAll = listOf(
    tv,
    anime,
  )
}
