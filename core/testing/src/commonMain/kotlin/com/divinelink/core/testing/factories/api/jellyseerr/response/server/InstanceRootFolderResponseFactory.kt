package com.divinelink.core.testing.factories.api.jellyseerr.response.server

import com.divinelink.core.network.jellyseerr.model.server.InstanceRootFolderResponse

object InstanceRootFolderResponseFactory {

  val tv = InstanceRootFolderResponse(
    id = 5,
    freeSpace = 1953233944576,
    path = "/data/tv",
  )

  val anime = InstanceRootFolderResponse(
    id = 6,
    freeSpace = 1953233944576,
    path = "/data/anime",
  )

  val movie = InstanceRootFolderResponse(
    id = 1,
    freeSpace = 1953237553152,
    path = "/data/movies",
  )

  val tvAll = listOf(
    tv,
    anime,
  )
}
