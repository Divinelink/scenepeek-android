package com.divinelink.core.network.media.model.find

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonResultApi(
  val id: Int,
  @SerialName("media_type") val mediaType: String,
  val name: String,
  @SerialName("profile_path") val profilePath: String?,
  val popularity: Double,
  val adult: Boolean,
  val gender: Int,
  @SerialName("known_for_department") val knownForDepartment: String,
  @SerialName("original_name") val originalName: String,
)
