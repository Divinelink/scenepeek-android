package com.divinelink.core.datastore.auth

import com.divinelink.core.datastore.auth.SavedState.JellyseerrAccount
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import kotlinx.serialization.Serializable

interface SavedState {
  val jellyseerrAccounts: Map<String, JellyseerrAccount>
  val jellyseerrAuthCookies: Map<String, String>

  val selectedJellyseerrAccountId: String?

  @Serializable
  data class JellyseerrAccount(
    val address: String,
    val account: String,
    val authMethod: JellyseerrAuthMethod,
    val password: String,
  )
}

@Serializable
data class ConcreteSavedState(
  override val jellyseerrAccounts: Map<String, JellyseerrAccount>,
  override val jellyseerrAuthCookies: Map<String, String>,
  override val selectedJellyseerrAccountId: String?,
) : SavedState

val SavedStateStorage.selectedJellyseerrId
  get() = savedState
    .value
    .selectedJellyseerrAccountId

val SavedStateStorage.selectedJellyseerrAccount
  get() = savedState
    .value
    .jellyseerrAccounts[selectedJellyseerrId]

val SavedStateStorage.selectedJellyseerrAuthCookie
  get() = savedState
    .value
    .jellyseerrAuthCookies[selectedJellyseerrId]

val SavedStateStorage.selectedJellyseerrHostAddress
  get() = savedState
    .value
    .jellyseerrAccounts[selectedJellyseerrId]?.address
