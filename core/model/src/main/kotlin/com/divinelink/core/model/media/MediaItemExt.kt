package com.divinelink.core.model.media

fun MediaItem.shareUrl(): String {
  val urlName = name
    .lowercase()
    .replace(":", "")
    .replace(regex = "[\\s|/]".toRegex(), replacement = "-")

  return when (this) {
    is MediaItem.Media.Movie -> "https://themoviedb.org/movie/$id-$urlName"
    is MediaItem.Media.TV -> "https://themoviedb.org/tv/$id-$urlName"
    is MediaItem.Person -> "https://themoviedb.org/person/$id-$urlName"
    MediaItem.Unknown -> ""
  }
}
