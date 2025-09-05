package com.divinelink.core.testing.factories.api.jellyseerr.response.server

import com.divinelink.core.network.jellyseerr.model.server.InstanceProfileResponse

object InstanceProfileResponseFactory {

  val any = InstanceProfileResponse(
    id = 1,
    name = "Any",
  )

  val sd = InstanceProfileResponse(
    id = 2,
    name = "SD",
  )

  val hd720 = InstanceProfileResponse(
    id = 3,
    name = "HD-720p",
  )

  val hd1080 = InstanceProfileResponse(
    id = 4,
    name = "HD-1080p",
  )

  val ultraHD = InstanceProfileResponse(
    id = 5,
    name = "Ultra-HD",
  )

  val hd7201080 = InstanceProfileResponse(
    id = 6,
    name = "HD - 720p/1080p",
  )

  val movie = listOf(
    any,
    sd,
    hd720,
    hd1080,
    ultraHD,
    hd7201080,
  )

  val tv = listOf(
    any,
    sd,
    hd720,
    hd1080,
    hd7201080,
  )
}
