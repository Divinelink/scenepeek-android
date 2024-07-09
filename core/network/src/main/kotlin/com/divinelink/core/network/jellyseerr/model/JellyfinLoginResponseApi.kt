package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import kotlinx.serialization.Serializable

@Serializable
data class JellyfinLoginResponseApi(
  val id: Long,
  val displayName: String,
  val avatar: String? = null,
  val requestCount: Long,
)

fun JellyfinLoginResponseApi.map() = JellyseerrAccountDetails(
  id = id,
  displayName = displayName,
  avatar = avatar ?: "",
  requestCount = requestCount,
)
