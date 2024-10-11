package com.divinelink.core.testing.factories.model.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails

object JellyseerrAccountDetailsFactory {

  fun jellyfin() = JellyseerrAccountDetails(
    id = 1,
    displayName = "Cup10",
    avatar = "http://localhost:5000/avatar",
    requestCount = 10,
    email = "cup10@proton.me",
    createdAt = "2023-08-19T00:00:00.000Z",
  )

  fun jellyseerr() = JellyseerrAccountDetails(
    id = 2,
    displayName = "Zabaob",
    avatar = "",
    requestCount = 20,
    email = "zabaob@proton.me",
    createdAt = "2022-08-20T00:00:00.000Z",
  )
}
