package com.divinelink.core.network.list.model.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatedByUserResponse(
  val id: String,
  val username: String,
  val name: String,
  @SerialName("avatar_path") val avatarPath: String? = null,
  @SerialName("gravatar_hash") val gravatarHash: String? = null,
)
