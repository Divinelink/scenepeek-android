package com.divinelink.core.network.media.model.details.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = CastSerializer::class)
sealed class CastApi {
  abstract val id: Long
  abstract val name: String
  abstract val profilePath: String?
  abstract val character: String
  abstract val order: Int
  abstract val knownForDepartment: String?

  @Serializable
  data class Movie(
    val adult: Boolean,
    val gender: Int,
    override val id: Long,
    @SerialName("cast_id") val castId: Int?,
    @SerialName("known_for_department") override val knownForDepartment: String,
    override val name: String,
    @SerialName("original_name") val originalName: String,
    val popularity: Double,
    @SerialName("profile_path") override val profilePath: String?,
    override val character: String,
    @SerialName("credit_id") val creditId: String,
    override val order: Int,
  ) : CastApi()

  @Serializable
  data class TV(
    val adult: Boolean,
    val gender: Int,
    override val id: Long,
    @SerialName("known_for_department") override val knownForDepartment: String,
    override val name: String,
    @SerialName("original_name") val originalName: String,
    val popularity: Double,
    @SerialName("profile_path") override val profilePath: String?,
    override val character: String,
    @SerialName("credit_id") val creditId: String,
    override val order: Int,
  ) : CastApi()
}
