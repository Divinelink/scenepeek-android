package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.extensions.isValidEmail
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi

fun JellyseerrAccountDetailsResponseApi.map(address: String) = JellyseerrAccountDetails(
  id = id,
  displayName = displayName,
  avatar = avatar?.let { address + it },
  requestCount = requestCount,
  email = if (email.isValidEmail()) email else null,
  createdAt = createdAt,
)
