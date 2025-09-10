package com.divinelink.core.data.auth

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.isJellyseerrEnabled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProdAuthRepository(private val savedStateStorage: SavedStateStorage) : AuthRepository {

  override val isJellyseerrEnabled: Flow<Boolean> = savedStateStorage
    .savedState
    .map { it.isJellyseerrEnabled() }

  override val jellyseerrAccount: Flow<SavedState.JellyseerrAccount?> = savedStateStorage
    .savedState
    .map { it.jellyseerr }
    .distinctUntilChanged()

  override suspend fun updateJellyseerrAccount(account: SavedState.JellyseerrAccount) {
    savedStateStorage.setJellyseerrAccount(account)
  }

  override suspend fun clearJellyseerrAccount() {
    savedStateStorage.setJellyseerrAccount(null)
  }
}
