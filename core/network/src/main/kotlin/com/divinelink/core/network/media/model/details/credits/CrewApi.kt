package com.divinelink.core.network.media.model.details.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrewApi(
  val adult: Boolean,
  @SerialName("credit_id") val creditId: String,
  val department: String,
  val gender: Int,
  val id: Long,
  val job: String,
  @SerialName("known_for_department") val knownForDepartment: String,
  val name: String,
  @SerialName("original_name") val originalName: String,
  val popularity: Double,
  @SerialName("profile_path") val profilePath: String?,
)
