package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import kotlinx.serialization.Serializable

@Serializable
data class JellyfinLoginResponseApi(
  val displayName: String,
  val avatar: String?,
  val requestCount: Int,
)

fun JellyfinLoginResponseApi.map() = JellyseerrAccountDetails(
  displayName = displayName,
  avatar = avatar ?: "",
  requestCount = requestCount,
)
