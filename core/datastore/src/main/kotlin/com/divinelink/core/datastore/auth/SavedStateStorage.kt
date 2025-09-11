package com.divinelink.core.datastore.auth

import com.divinelink.core.datastore.auth.SavedState.JellyseerrAccount
import kotlinx.coroutines.flow.StateFlow

interface SavedStateStorage {
  val savedState: StateFlow<SavedState>

  suspend fun setJellyseerrAccount(account: JellyseerrAccount)
  suspend fun setJellyseerrAuthCookie(cookie: String)

  suspend fun clearSelectedJellyseerrAccount()
}
