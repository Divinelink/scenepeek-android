package com.andreolas.movierama.details.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Video(
  val id: String,
  val name: String,
  val officialTrailer: Boolean,
  val site: VideoSite?,
  val key: String,
) {
  val url: String
    get() = site?.url + key
}

enum class VideoSite(val url: String) {
  YouTube("https://www.youtube.com/watch?v="),
  Vimeo("https://vimeo.com/"),
}
