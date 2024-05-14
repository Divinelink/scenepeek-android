package com.andreolas.factories

import com.divinelink.core.model.details.Video
import com.divinelink.core.model.details.VideoSite

object VideoFactory {

  fun Youtube(): Video = Video(
    id = "id",
    name = "name",
    officialTrailer = true,
    site = VideoSite.YouTube,
    key = "key",
  )

  fun Vimeo() = Video(
    id = "definitiones",
    name = "Stacey Tyler",
    officialTrailer = true,
    site = VideoSite.Vimeo,
    key = "contentiones",
  )
}
