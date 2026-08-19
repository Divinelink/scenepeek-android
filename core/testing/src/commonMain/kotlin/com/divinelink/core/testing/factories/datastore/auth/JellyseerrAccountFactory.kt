package com.divinelink.core.testing.factories.datastore.auth

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod

object JellyseerrAccountFactory {

  fun cup10() = SavedState.JellyseerrCredentials(
    address = "http://localhost:8080",
    account = "Cup10",
    authMethod = SeerrAuthMethod.JELLYFIN,
    password = "123456789",
  )

  fun zabaob() = SavedState.JellyseerrCredentials(
    address = "http://localhost:5055",
    account = "Zabaob",
    authMethod = SeerrAuthMethod.SEERR,
    password = "987654321",
  )
}
