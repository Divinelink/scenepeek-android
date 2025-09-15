package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.auth.ConcreteSavedState
import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.datastore.auth.SavedState.JellyseerrCredentials
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestSavedStateStorage(
  jellyseerrCredentials: Map<String, JellyseerrCredentials> = emptyMap(),
  jellyseerrProfiles: Map<String, JellyseerrProfile> = emptyMap(),
  jellyseerrAuthCookies: Map<String, String> = emptyMap(),
  selectedJellyseerrAccountId: String? = null,
) : SavedStateStorage {

  private var accountCounter = 1
  private var accountId = "account_$accountCounter"

  private val _savedState = MutableStateFlow(
    ConcreteSavedState(
      jellyseerrCredentials = jellyseerrCredentials,
      jellyseerrProfiles = jellyseerrProfiles,
      jellyseerrAuthCookies = jellyseerrAuthCookies,
      selectedJellyseerrId = selectedJellyseerrAccountId,
    ),
  )

  override val savedState: StateFlow<SavedState> = _savedState

  override suspend fun setJellyseerrCredentials(credentials: JellyseerrCredentials) {
    _savedState.value = _savedState.value.copy(
      selectedJellyseerrId = accountId,
      jellyseerrCredentials = _savedState.value.jellyseerrCredentials + (accountId to credentials),
    )
  }

  override suspend fun setJellyseerrProfile(profile: JellyseerrProfile) {
    _savedState.value = _savedState.value.copy(
      selectedJellyseerrId = accountId,
      jellyseerrProfiles = _savedState.value.jellyseerrProfiles + (accountId to profile),
    )
  }

  override suspend fun setJellyseerrAuthCookie(cookie: String) {
    _savedState.value = _savedState.value.copy(
      selectedJellyseerrId = accountId,
      jellyseerrAuthCookies = _savedState.value.jellyseerrAuthCookies + (accountId to cookie),
    )

    accountCounter++
  }

  override suspend fun clearSelectedJellyseerrAccount() {
    val currentState = _savedState.value
    val selectedId = currentState.selectedJellyseerrId ?: return

    _savedState.value = currentState.copy(
      selectedJellyseerrId = null,
      jellyseerrProfiles = currentState.jellyseerrProfiles - selectedId,
      jellyseerrCredentials = currentState.jellyseerrCredentials - selectedId,
      jellyseerrAuthCookies = currentState.jellyseerrAuthCookies - selectedId,
    )
  }
}
