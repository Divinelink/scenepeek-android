package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.extensions.isValidEmail
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.network.jellyseerr.model.JellyseerrProfileResponse

fun JellyseerrProfileResponse.map(address: String) = JellyseerrProfile(
  id = id,
  displayName = displayName,
  avatar = avatar?.let { address + it },
  requestCount = requestCount,
  email = if (email.isValidEmail()) email else null,
  createdAt = createdAt,
  permissions = ProfilePermission.decode(permissions),
)
