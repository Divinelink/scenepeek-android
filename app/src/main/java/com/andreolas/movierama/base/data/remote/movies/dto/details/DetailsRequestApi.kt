package com.andreolas.movierama.base.data.remote.movies.dto.details

import com.andreolas.movierama.home.domain.model.MediaType
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
    endpoint = MediaType.MOVIE.value
  )

  @Serializable
  data class TV(
    @SerialName("series_id")
    val seriesId: Int,
  ) : DetailsRequestApi(
    id = seriesId,
    endpoint = MediaType.TV.value
  )
}
