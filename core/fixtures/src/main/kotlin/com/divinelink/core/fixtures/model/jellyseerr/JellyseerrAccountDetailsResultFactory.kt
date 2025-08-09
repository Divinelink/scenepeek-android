package com.divinelink.core.fixtures.model.jellyseerr

import com.divinelink.core.domain.jellyseerr.JellyseerrAccountDetailsResult

object JellyseerrAccountDetailsResultFactory {

  fun initial() = JellyseerrAccountDetailsResult(
    address = "",
    accountDetails = null,
  )

  fun signedOut() = JellyseerrAccountDetailsResult(
    address = "http://localhost:5055",
    accountDetails = null,
  )

  fun jellyfin() = JellyseerrAccountDetailsResult(
    address = "http://localhost:5055",
    accountDetails = JellyseerrAccountDetailsFactory.jellyfin(),
  )

  fun jellyseerr() = JellyseerrAccountDetailsResult(
    address = "http://localhost:5055",
    accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
  )
}
