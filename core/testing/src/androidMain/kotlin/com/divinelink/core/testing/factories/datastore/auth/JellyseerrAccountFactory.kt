package com.divinelink.core.testing.factories.datastore.auth

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod

object JellyseerrAccountFactory {

  fun cup10() = SavedState.JellyseerrCredentials(
    address = "http://localhost:8080",
    account = "Cup10",
    authMethod = JellyseerrAuthMethod.JELLYFIN,
    password = "123456789",
  )

  fun zabaob() = SavedState.JellyseerrCredentials(
    address = "http://localhost:5055",
    account = "Zabaob",
    authMethod = JellyseerrAuthMethod.JELLYSEERR,
    password = "987654321",
  )
}
