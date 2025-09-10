package com.divinelink.core.datastore.auth

import com.divinelink.core.datastore.auth.SavedState.JellyseerrAccount
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import kotlinx.serialization.Serializable

interface SavedState {
  val jellyseerr: JellyseerrAccount?

  @Serializable
  data class JellyseerrAccount(
    val address: String,
    val account: String,
    val authMethod: JellyseerrAuthMethod,
    val password: String,
  )
}

@Serializable
data class ConcreteSavedState(override val jellyseerr: JellyseerrAccount? = null) : SavedState
