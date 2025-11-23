package com.divinelink.core.datastore.auth

import com.divinelink.core.datastore.auth.SavedState.JellyseerrCredentials
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.session.TmdbSession
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

interface SavedState {
  val tmdbAccount: AccountDetails?
  val tmdbSession: TmdbSession?

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
  override val tmdbAccount: AccountDetails?,
  override val tmdbSession: TmdbSession?,
  override val jellyseerrCredentials: Map<String, JellyseerrCredentials>,
  override val jellyseerrProfiles: Map<String, JellyseerrProfile>,
  override val jellyseerrAuthCookies: Map<String, String>,
  override val selectedJellyseerrId: String?,
) : SavedState

val SavedStateStorage.observedSelectedJellyseerrId
  get() = savedState
    .map { it.selectedJellyseerrId }
    .distinctUntilChanged()

val SavedState.isJellyseerrEnabled
  get() = jellyseerrCredentials.keys.isNotEmpty() && jellyseerrAuthCookies.keys.isNotEmpty()

val SavedState.profilePermissions
  get() = jellyseerrProfiles[selectedJellyseerrId]?.permissions ?: emptyList()

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

val SavedStateStorage.observedTmdbSession
  get() = savedState
    .map { it.tmdbSession }
    .distinctUntilChanged()

val SavedStateStorage.accessToken
  get() = savedState
    .value
    .tmdbSession
    ?.accessToken
    ?.accessToken
