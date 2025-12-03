package com.divinelink.core.network.media.model

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MediaRequestApi(
  open val id: Int,
  open val mediaType: MediaType,
) {

  @Serializable
  data class Movie(
    @SerialName("movie_id")
    val movieId: Int,
  ) : MediaRequestApi(
    id = movieId,
    mediaType = MediaType.MOVIE,
  )

  @Serializable
  data class TV(
    @SerialName("series_id")
    val seriesId: Int,
  ) : MediaRequestApi(
    id = seriesId,
    mediaType = MediaType.TV,
  )

  data object Unknown : MediaRequestApi(
    id = -1,
    mediaType = MediaType.UNKNOWN,
  )
}
