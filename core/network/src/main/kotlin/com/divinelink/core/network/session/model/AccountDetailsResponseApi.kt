package com.divinelink.core.network.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsResponseApi(
  val id: Int,
  val username: String,
  val name: String,
  val avatar: AvatarResponseApi,
)

@Serializable
data class AvatarResponseApi(val tmdb: TmdbAvatarResponseApi)

@Serializable
data class TmdbAvatarResponseApi(@SerialName("avatar_path") val avatarPath: String?)
