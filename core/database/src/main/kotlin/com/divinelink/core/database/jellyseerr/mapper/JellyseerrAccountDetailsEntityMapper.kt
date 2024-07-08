package com.divinelink.core.database.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import comdivinelinkcoredatabase.JellyseerrAccountDetailsEntity

fun JellyseerrAccountDetails.mapToEntity() = JellyseerrAccountDetailsEntity(
  id = id,
  displayName = displayName,
  avatar = avatar,
  requestCount = requestCount,
)

fun JellyseerrAccountDetailsEntity.map() = JellyseerrAccountDetails(
  id = id,
  displayName = displayName,
  avatar = avatar ?: "",
  requestCount = requestCount,
)
