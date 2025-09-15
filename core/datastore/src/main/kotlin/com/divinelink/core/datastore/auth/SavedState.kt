package com.divinelink.core.datastore.auth

import com.divinelink.core.datastore.auth.SavedState.JellyseerrCredentials
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import kotlinx.serialization.Serializable

interface SavedState {
  val jellyseerrCredentials: Map<String, JellyseerrCredentials>
  val jellyseerrProfiles: Map<String, JellyseerrProfile>
  val jellyseerrAuthCookies: Map<String, String>

  val selectedJellyseerrId: String?

  @Serializable
  data class JellyseerrCredentials(
    val address: String,
    val account: String,
    val authMethod: JellyseerrAuthMethod,
    val password: String,
  )
}

@Serializable
data class ConcreteSavedState(
  override val jellyseerrCredentials: Map<String, JellyseerrCredentials>,
  override val jellyseerrProfiles: Map<String, JellyseerrProfile>,
  override val jellyseerrAuthCookies: Map<String, String>,
  override val selectedJellyseerrId: String?,
) : SavedState

val SavedStateStorage.selectedJellyseerrId
  get() = savedState
    .value
    .selectedJellyseerrId

val SavedStateStorage.selectedJellyseerrCredentials
  get() = savedState
    .value
    .jellyseerrCredentials[selectedJellyseerrId]

val SavedStateStorage.selectedJellyseerrProfile
  get() = savedState
    .value
    .jellyseerrProfiles[selectedJellyseerrId]

val SavedStateStorage.selectedJellyseerrAuthCookie
  get() = savedState
    .value
    .jellyseerrAuthCookies[selectedJellyseerrId]

val SavedStateStorage.selectedJellyseerrHostAddress
  get() = savedState
    .value
    .jellyseerrCredentials[selectedJellyseerrId]?.address
