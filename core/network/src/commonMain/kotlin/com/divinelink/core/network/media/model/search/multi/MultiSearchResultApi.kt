package com.divinelink.core.network.media.model.search.multi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchResultApi(
  @SerialName("id") val id: Int,
  @SerialName("adult") val adult: Boolean,
  @SerialName("backdrop_path") val backdropPath: String? = null,
  @SerialName("title") val title: String? = null,
  @SerialName("name") val name: String? = null,
  @SerialName("original_language") val originalLanguage: String? = null,
  @SerialName("original_name") val originalName: String? = null,
  @SerialName("overview") val overview: String? = null,
  @SerialName("poster_path") val posterPath: String? = null,
  @SerialName("profile_path") val profilePath: String? = null,
  @SerialName("media_type") val mediaType: String?,
  @SerialName("genre_ids") val genreIds: List<Int>? = null,
  val gender: Int? = null,
  @SerialName("popularity") val popularity: Double,
  @SerialName("first_air_date") val firstAirDate: String? = null,
  @SerialName("release_date") val releaseDate: String? = null,
  @SerialName("vote_average") val voteAverage: Double? = null,
  @SerialName("vote_count") val voteCount: Int? = null,
  @SerialName("origin_country") val originCountry: List<String>? = null,
  @SerialName("original_title") val originalTitle: String? = null,
  @SerialName("known_for_department") val knownForDepartment: String? = null,
)
