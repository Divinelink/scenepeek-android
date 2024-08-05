package com.divinelink.core.network.details.person.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonCrewCreditApi(
  val adult: Boolean,
  @SerialName("backdrop_path") val backdropPath: String?,
  @SerialName("genre_ids") val genreIds: List<Int>,
  val id: Int,
  @SerialName("origin_country") val originCountry: List<String>? = null,
  @SerialName("original_language") val originalLanguage: String,
  @SerialName("original_title") val originalTitle: String? = null,
  @SerialName("original_name") val originalName: String? = null,
  val overview: String,
  val popularity: Double,
  @SerialName("poster_path") val posterPath: String?,
  @SerialName("release_date") val releaseDate: String? = null,
  @SerialName("first_air_date") val firstAirDate: String? = null,
  val title: String? = null,
  val name: String? = null,
  val video: Boolean? = null,
  @SerialName("vote_average") val voteAverage: Double,
  @SerialName("vote_count") val voteCount: Int,
  @SerialName("credit_id") val creditId: String,
  val department: String,
  val job: String,
  @SerialName("media_type") val mediaType: String,
  @SerialName("episode_count") val episodeCount: Int? = null,
)
