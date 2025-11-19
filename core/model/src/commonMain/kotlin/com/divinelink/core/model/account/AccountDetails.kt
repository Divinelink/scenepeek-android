package com.divinelink.core.model.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountDetails(
  val id: Int,
  val username: String,
  val name: String,
  val tmdbAvatarPath: String?,
)

fun AccountDetails.getAvatarUrl(imageUrl: String): String? {
  return tmdbAvatarPath?.let { "${imageUrl}$it" }
}
