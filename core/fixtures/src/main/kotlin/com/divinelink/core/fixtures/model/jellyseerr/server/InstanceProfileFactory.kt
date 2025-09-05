package com.divinelink.core.fixtures.model.jellyseerr.server

import com.divinelink.core.model.jellyseerr.server.InstanceProfile

object InstanceProfileFactory {

  val any = InstanceProfile(
    id = 1,
    name = "Any",
  )

  val sd = InstanceProfile(
    id = 2,
    name = "SD",
  )

  val hd720 = InstanceProfile(
    id = 3,
    name = "HD-720p",
  )

  val hd1080 = InstanceProfile(
    id = 4,
    name = "HD-1080p",
  )

  val ultraHD = InstanceProfile(
    id = 5,
    name = "Ultra-HD",
  )

  val hd7201080 = InstanceProfile(
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
