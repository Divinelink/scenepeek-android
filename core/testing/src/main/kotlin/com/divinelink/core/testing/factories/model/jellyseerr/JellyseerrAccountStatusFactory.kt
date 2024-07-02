package com.divinelink.core.testing.factories.model.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrAccountStatus
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod

object JellyseerrAccountStatusFactory {

  fun jellyfin() = JellyseerrAccountStatus(
    username = "Zabaob",
    address = "http://localhost:5000",
    signInMethod = JellyseerrLoginMethod.JELLYFIN,
  )

  fun jellyseerr() = JellyseerrAccountStatus(
    username = "Cup10",
    address = "http://localhost:5000",
    signInMethod = JellyseerrLoginMethod.JELLYSEERR,
  )
}
