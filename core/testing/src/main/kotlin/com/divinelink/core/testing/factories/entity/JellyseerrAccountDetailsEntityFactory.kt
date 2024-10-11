package com.divinelink.core.testing.factories.entity

import com.divinelink.core.database.JellyseerrAccountDetailsEntity

object JellyseerrAccountDetailsEntityFactory {

  fun jellyfin() = JellyseerrAccountDetailsEntity(
    id = 1,
    displayName = "Cup10",
    requestCount = 10,
    avatar = "http://localhost:5000/avatar",
    email = "cup10@proton.me",
    createdAt = "2023-08-19T00:00:00.000Z",
  )

  fun jellyseerr() = JellyseerrAccountDetailsEntity(
    id = 2,
    displayName = "Zabaob",
    requestCount = 20,
    avatar = null,
    email = "zabaob@proton.me",
    createdAt = "2022-08-20T00:00:00.000Z",
  )
}
