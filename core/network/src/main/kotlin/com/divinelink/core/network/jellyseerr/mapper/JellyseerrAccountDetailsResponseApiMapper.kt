package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.extensions.isValidEmail
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi

fun JellyseerrAccountDetailsResponseApi.map() = JellyseerrAccountDetails(
  id = id,
  displayName = displayName,
  avatar = avatar,
  requestCount = requestCount,
  email = if (email.isValidEmail()) email else null,
  createdAt = createdAt,
)
