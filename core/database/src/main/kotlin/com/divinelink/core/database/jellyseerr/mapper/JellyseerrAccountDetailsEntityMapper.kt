package com.divinelink.core.database.jellyseerr.mapper

import com.divinelink.core.database.JellyseerrAccountDetailsEntity
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails

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
