package com.divinelink.core.model.media

import kotlinx.serialization.json.Json

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

fun MediaItem.toStub(): MediaReference = MediaReference(
  mediaId = id,
  mediaType = mediaType,
)

fun MediaItem.encodeToString() = Json.encodeToString<MediaItem>(this)
fun String.decodeToMediaItem() = Json.decodeFromString<MediaItem>(this)
