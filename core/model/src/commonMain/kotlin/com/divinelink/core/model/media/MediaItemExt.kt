package com.divinelink.core.model.media

import kotlinx.serialization.json.Json

fun MediaItem.shareUrl(
  domain: String,
): String {
  val scheme = "https://$domain"
  val urlName = name
    .lowercase()
    .replace(":", "")
    .replace(regex = "[\\s|/]".toRegex(), replacement = "-")

  return when (this) {
    is MediaItem.Media.Movie -> "$scheme/movie/$id-$urlName"
    is MediaItem.Media.TV -> "$scheme/tv/$id-$urlName"
    is MediaItem.Person -> "$scheme/person/$id-$urlName"
    MediaItem.Unknown -> ""
  }
}

fun MediaItem.toStub(): MediaReference = MediaReference(
  mediaId = id,
  mediaType = mediaType,
)

fun MediaItem.encodeToString() = Json.encodeToString<MediaItem>(this)
fun String.decodeToMediaItem() = Json.decodeFromString<MediaItem>(this)
