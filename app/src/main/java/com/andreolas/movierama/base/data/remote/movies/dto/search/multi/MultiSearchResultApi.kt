package com.andreolas.movierama.base.data.remote.movies.dto.search.multi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchResultApi(
  @SerialName("id") val id: Int,
  @SerialName("adult") val adult: Boolean,
  @SerialName("backdrop_path") val backdropPath: String?,
  @SerialName("title") val title: String?,
  @SerialName("name") val name: String?,
  @SerialName("original_language") val originalLanguage: String,
  @SerialName("original_name") val originalName: String?,
  @SerialName("overview") val overview: String,
  @SerialName("poster_path") val posterPath: String?,
  @SerialName("media_type") val mediaType: String,
  @SerialName("genre_ids") val genreIds: List<Int>,
  @SerialName("popularity") val popularity: Double,
  @SerialName("first_air_date") val firstAirDate: String?,
  @SerialName("release_date") val releaseDate: String,
  @SerialName("vote_average") val voteAverage: Double,
  @SerialName("vote_count") val voteCount: Int,
  @SerialName("origin_country") val originCountry: List<String>,
)
