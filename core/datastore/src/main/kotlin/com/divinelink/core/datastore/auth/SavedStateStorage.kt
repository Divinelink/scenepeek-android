package com.divinelink.core.datastore.auth

import com.divinelink.core.datastore.auth.SavedState.JellyseerrCredentials
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import kotlinx.coroutines.flow.StateFlow

interface SavedStateStorage {
  val savedState: StateFlow<SavedState>

  suspend fun setJellyseerrCredentials(credentials: JellyseerrCredentials)
  suspend fun setJellyseerrProfile(profile: JellyseerrProfile)
  suspend fun setJellyseerrAuthCookie(cookie: String)

  suspend fun clearSelectedJellyseerrAccount()

  suspend fun setTMDBAccount(account: AccountDetails)
  suspend fun clearTMDBAccount()
}
