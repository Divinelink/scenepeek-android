package com.divinelink.core.network.media.model.details.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDetailsApi(
  @SerialName("avatar_path") val avatarPath: String?,
  val name: String,
  val rating: Double?,
  val username: String,
)
