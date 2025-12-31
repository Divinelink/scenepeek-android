package com.divinelink.core.network.details.person.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDetailsApi(
  val biography: String,
  val birthday: String?,
  val deathday: String?,
  val gender: Int,
  val homepage: String?,
  val id: Long,
  @SerialName("imdb_id") val imdbId: String?,
  @SerialName("known_for_department") val knownForDepartment: String?,
  val name: String,
  @SerialName("place_of_birth") val placeOfBirth: String?,
  val popularity: Double,
  @SerialName("profile_path") val profilePath: String?,
  @SerialName("also_known_as") val alsoKnownAs: List<String>,
)
