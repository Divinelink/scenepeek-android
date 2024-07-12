package com.divinelink.core.network.media.model.details

import com.divinelink.core.model.media.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class DetailsRequestApi(
  open val id: Int,
  open val endpoint: String,
) {

  @Serializable
  data class Movie(
    @SerialName("movie_id")
    val movieId: Int,
  ) : DetailsRequestApi(
    id = movieId,
    endpoint = MediaType.MOVIE.value,
  )

  @Serializable
  data class TV(
    @SerialName("series_id")
    val seriesId: Int,
  ) : DetailsRequestApi(
    id = seriesId,
    endpoint = MediaType.TV.value,
  )

  data object Unknown : DetailsRequestApi(
    id = -1,
    endpoint = "",
  )
}
