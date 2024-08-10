package com.divinelink.core.network.media.model.details.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesCreatorApi(
  val id: Long,
  @SerialName("credit_id") val creditId: String,
  val name: String,
  @SerialName("original_name") val originalName: String,
  val gender: Int,
  @SerialName("profile_path") val profilePath: String?,
  @SerialName("known_for_department") val knownForDepartment: String,
)
