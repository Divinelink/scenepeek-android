package com.andreolas.factories

import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.model.VideoSite

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
