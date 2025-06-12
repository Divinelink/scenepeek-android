package com.divinelink.core.testing.factories.api.jellyseerr

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi

object JellyseerrRequestMediaBodyApiFactory {

  fun tv() = JellyseerrRequestMediaBodyApi(
    mediaType = MediaType.TV.value,
    mediaId = 8096,
    is4k = false,
    seasons = listOf(1, 2, 3),
  )

  fun movie() = JellyseerrRequestMediaBodyApi(
    mediaType = MediaType.MOVIE.value,
    mediaId = 8080,
    is4k = false,
    seasons = emptyList(),
  )
}
