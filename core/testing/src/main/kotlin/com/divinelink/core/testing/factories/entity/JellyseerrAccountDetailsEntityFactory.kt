package com.divinelink.core.testing.factories.entity

import com.divinelink.core.database.JellyseerrAccountDetailsEntity

object JellyseerrAccountDetailsEntityFactory {

  fun jellyfin() = JellyseerrAccountDetailsEntity(
    id = 1,
    displayName = "Cup10",
    requestCount = 10,
    avatar = "http://localhost:5000/avatar",
  )

  fun jellyseerr() = JellyseerrAccountDetailsEntity(
    id = 2,
    displayName = "Zabaob",
    requestCount = 20,
    avatar = null,
  )
}
