package com.divinelink.core.testing.factories.api.jellyseerr.response

import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi

object JellyfinLoginResponseApiFactory {

  fun jellyfin() = JellyfinLoginResponseApi(
    id = 1,
    displayName = "Cup10",
    avatar = "http://localhost:5000/avatar",
    requestCount = 10,
  )

  fun jellyseerr() = JellyfinLoginResponseApi(
    id = 2,
    displayName = "Zabaob",
    avatar = null,
    requestCount = 20,
  )
}
