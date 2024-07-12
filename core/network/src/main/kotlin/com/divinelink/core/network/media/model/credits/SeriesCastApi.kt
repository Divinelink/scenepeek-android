package com.divinelink.core.network.media.model.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesCastApi(
  val id: Int,
  val name: String,
  val adult: Boolean,
  val gender: Int,
  val popularity: Double,
  val roles: List<RolesApi>,
  val order: Int,
  @SerialName("known_for_department") val knownForDepartment: String,
  @SerialName("original_name") val originalName: String,
  @SerialName("profile_path") val profilePath: String?,
  @SerialName("total_episode_count") val totalEpisodeCount: Int,
)
