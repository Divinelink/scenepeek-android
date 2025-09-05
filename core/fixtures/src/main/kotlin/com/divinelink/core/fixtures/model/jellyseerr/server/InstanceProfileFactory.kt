package com.divinelink.core.fixtures.model.jellyseerr.server

import com.divinelink.core.model.jellyseerr.server.InstanceProfile

object InstanceProfileFactory {

  val any = InstanceProfile(
    id = 1,
    name = "Any",
    isDefault = false,
  )

  val sd = InstanceProfile(
    id = 2,
    name = "SD",
    isDefault = false,
  )

  val hd720 = InstanceProfile(
    id = 3,
    name = "HD-720p",
    isDefault = false,
  )

  val hd1080 = InstanceProfile(
    id = 4,
    name = "HD-1080p",
    isDefault = false,
  )

  val ultraHD = InstanceProfile(
    id = 5,
    name = "Ultra-HD",
    isDefault = false,
  )

  val hd7201080 = InstanceProfile(
    id = 6,
    name = "HD - 720p/1080p",
    isDefault = true,
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
