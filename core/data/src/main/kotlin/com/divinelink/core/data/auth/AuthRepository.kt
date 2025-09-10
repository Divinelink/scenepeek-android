package com.divinelink.core.data.auth

import com.divinelink.core.datastore.auth.SavedState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  val isJellyseerrEnabled: Flow<Boolean>
  val jellyseerrAccount: Flow<SavedState.JellyseerrAccount?>

  suspend fun updateJellyseerrAccount(account: SavedState.JellyseerrAccount)
  suspend fun clearJellyseerrAccount()
}
