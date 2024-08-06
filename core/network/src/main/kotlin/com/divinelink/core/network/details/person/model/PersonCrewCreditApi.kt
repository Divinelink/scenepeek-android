package com.divinelink.core.network.details.person.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonCrewCreditApi(
  val id: Int,
  val adult: Boolean,
  val overview: String,
  val popularity: Double,
  val department: String,
  val job: String,
  @SerialName("backdrop_path") val backdropPath: String?,
  @SerialName("genre_ids") val genreIds: List<Int>,
  @SerialName("original_language") val originalLanguage: String,
  @SerialName("poster_path") val posterPath: String?,
  @SerialName("vote_average") val voteAverage: Double,
  @SerialName("vote_count") val voteCount: Long,
  @SerialName("credit_id") val creditId: String,
  @SerialName("media_type") val mediaType: String,
  // Movie only properties
  val title: String? = null,
  val video: Boolean? = null,
  @SerialName("original_title") val originalTitle: String? = null,
  @SerialName("release_date") val releaseDate: String? = null,
  // TV only properties
  val name: String? = null,
  @SerialName("origin_country") val originCountry: List<String>? = null,
  @SerialName("original_name") val originalName: String? = null,
  @SerialName("first_air_date") val firstAirDate: String? = null,
  @SerialName("episode_count") val episodeCount: Int? = null,
)
