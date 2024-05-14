package com.divinelink.core.network.movies.model.details.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = CastSerializer::class)
sealed class Cast {
  abstract val id: Int
  abstract val name: String
  abstract val profilePath: String?
  abstract val character: String
  abstract val order: Int

  @Serializable
  data class Movie(
    val adult: Boolean,
    val gender: Int,
    override val id: Int,
    @SerialName("cast_id") val castId: Int?,
    @SerialName("known_for_department") val knownForDepartment: String,
    override val name: String,
    @SerialName("original_name") val originalName: String,
    val popularity: Double,
    @SerialName("profile_path") override val profilePath: String?,
    override val character: String,
    @SerialName("credit_id") val creditId: String,
    override val order: Int,
  ) : Cast()

  @Serializable
  data class TV(
    val adult: Boolean,
    val gender: Int,
    override val id: Int,
    @SerialName("known_for_department") val knownForDepartment: String,
    override val name: String,
    @SerialName("original_name") val originalName: String,
    val popularity: Double,
    @SerialName("profile_path") override val profilePath: String?,
    override val character: String,
    @SerialName("credit_id") val creditId: String,
    override val order: Int,
  ) : Cast()
}
