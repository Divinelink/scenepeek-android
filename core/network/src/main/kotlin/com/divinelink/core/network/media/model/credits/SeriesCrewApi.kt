package com.divinelink.core.network.media.model.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesCrewApi(
  val id: Int,
  val adult: Boolean,
  val gender: Int,
  @SerialName("known_for_department") val knownForDepartment: String,
  val name: String,
  @SerialName("original_name") val originalName: String,
  val popularity: Double,
  @SerialName("profile_path") val profilePath: String?,
  val jobs: List<JobsApi>,
  val department: String,
  @SerialName("total_episode_count") val totalEpisodeCount: Int,
)
