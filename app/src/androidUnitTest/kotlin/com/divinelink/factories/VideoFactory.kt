package com.divinelink.factories

import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite

object VideoFactory {

  fun Youtube(): Video = Video(
    id = "youtubeId",
    name = "name",
    officialTrailer = true,
    site = VideoSite.YouTube,
    key = "key",
  )

  fun Vimeo() = Video(
    id = "vimeoId",
    name = "Stacey Tyler",
    officialTrailer = true,
    site = VideoSite.Vimeo,
    key = "contentiones",
  )

  fun RickRoll() = Video(
    id = "dQw4w9WgXcQ",
    name = "Rick Astley - Never Gonna Give You Up (Video)",
    officialTrailer = false,
    site = VideoSite.YouTube,
    key = "dQw4w9WgXcQ",
  )

  fun all() = listOf(RickRoll(), Youtube(), Vimeo())
}
