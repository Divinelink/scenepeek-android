package com.andreolas.movierama.base.data.remote.movies.dto.details.credits

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Cast(
  open val id: Int,
  open val name: String,
  open val profilePath: String?,
  open val character: String,
  open val order: Int,
) {

  data class Movie(
    val adult: Boolean,
    @SerialName("cast_id")
    val castId: Int?,
    override val character: String,
    @SerialName("credit_id")
    val creditId: String,
    val gender: Int,
    override val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    override val name: String,
    override val order: Int,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerialName("profile_path")
    override val profilePath: String?,
  ) : Cast(
    id = id,
    name = name,
    profilePath = profilePath ?: "",
    character = character,
    order = order,
  )

  data class TV(
    val adult: Boolean,
    val gender: Int,
    override val id: Int,
    @SerialName("known_for_department") val knownForDepartment: String,
    override val name: String,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerialName("profile_path")
    override val profilePath: String?,
    override val character: String,
    @SerialName("credit_id")
    val creditId: String,
    override val order: Int,
  ) : Cast(
    id = id,
    name = name,
    profilePath = profilePath ?: "",
    character = character,
    order = order,
  )
}
