package com.divinelink.core.model.details.video

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
