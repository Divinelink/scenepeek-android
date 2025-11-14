package com.divinelink.core.fixtures.model.jellyseerr

import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult

object JellyseerrAccountDetailsResultFactory {

  fun initial() = JellyseerrProfileResult(
    address = "",
    profile = null,
  )

  fun signedOut() = JellyseerrProfileResult(
    address = "http://localhost:5055",
    profile = null,
  )

  fun jellyfin() = JellyseerrProfileResult(
    address = "http://localhost:5055",
    profile = JellyseerrProfileFactory.jellyfin(),
  )

  fun jellyseerr() = JellyseerrProfileResult(
    address = "http://localhost:5055",
    profile = JellyseerrProfileFactory.jellyseerr(),
  )
}
