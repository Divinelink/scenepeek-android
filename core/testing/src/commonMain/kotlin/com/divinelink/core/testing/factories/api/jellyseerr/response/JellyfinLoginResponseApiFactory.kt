package com.divinelink.core.testing.factories.api.jellyseerr.response

import com.divinelink.core.network.jellyseerr.model.JellyseerrProfileResponse

object JellyfinLoginResponseApiFactory {

  fun jellyfin() = JellyseerrProfileResponse(
    id = 1,
    displayName = "Cup10",
    avatar = "http://localhost:5000/avatar",
    requestCount = 10,
    email = "cup10@proton.me",
    createdAt = "August 19, 2023",
    permissions = 7082016,
  )

  fun jellyseerr() = JellyseerrProfileResponse(
    id = 2,
    displayName = "Zabaob",
    avatar = null,
    requestCount = 20,
    email = "zabaob@proton.me",
    createdAt = "August 20, 2022",
    permissions = 2,
  )
}
