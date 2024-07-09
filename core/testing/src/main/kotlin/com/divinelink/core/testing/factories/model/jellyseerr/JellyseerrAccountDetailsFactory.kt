package com.divinelink.core.testing.factories.model.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails

object JellyseerrAccountDetailsFactory {

  fun jellyfin() = JellyseerrAccountDetails(
    id = 1,
    displayName = "Cup10",
    avatar = "http://localhost:5000/avatar",
    requestCount = 100,
  )

  fun jellyseerr() = JellyseerrAccountDetails(
    id = 2,
    displayName = "Zabaob",
    avatar = "http://localhost:5000/avatar",
    requestCount = 120,
  )
}
