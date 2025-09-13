package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.auth.ConcreteSavedState
import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.datastore.auth.SavedState.JellyseerrAccount
import com.divinelink.core.datastore.auth.SavedStateStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestSavedStateStorage(
  jellyseerrAccounts: Map<String, JellyseerrAccount> = emptyMap(),
  jellyseerrAuthCookies: Map<String, String> = emptyMap(),
  selectedJellyseerrAccountId: String? = null,
) : SavedStateStorage {

  private var accountCounter = 1
  private var accountId = "account_$accountCounter"

  private val _savedState = MutableStateFlow(
    ConcreteSavedState(
      jellyseerrAccounts = jellyseerrAccounts,
      jellyseerrAuthCookies = jellyseerrAuthCookies,
      selectedJellyseerrAccountId = selectedJellyseerrAccountId,
    ),
  )

  override val savedState: StateFlow<SavedState> = _savedState

  override suspend fun setJellyseerrAccount(account: JellyseerrAccount) {
    _savedState.value = _savedState.value.copy(
      selectedJellyseerrAccountId = accountId,
      jellyseerrAccounts = _savedState.value.jellyseerrAccounts + (accountId to account),
    )
  }

  override suspend fun setJellyseerrAuthCookie(cookie: String) {
    _savedState.value = _savedState.value.copy(
      selectedJellyseerrAccountId = accountId,
      jellyseerrAuthCookies = _savedState.value.jellyseerrAuthCookies + (accountId to cookie),
    )

    accountCounter++
  }

  override suspend fun clearSelectedJellyseerrAccount() {
    val currentState = _savedState.value
    val selectedId = currentState.selectedJellyseerrAccountId ?: return

    _savedState.value = currentState.copy(
      selectedJellyseerrAccountId = null,
      jellyseerrAccounts = currentState.jellyseerrAccounts - selectedId,
      jellyseerrAuthCookies = currentState.jellyseerrAuthCookies - selectedId,
    )
  }
}
