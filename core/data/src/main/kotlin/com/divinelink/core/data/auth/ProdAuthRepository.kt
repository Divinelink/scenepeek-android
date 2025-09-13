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

  override val jellyseerrAccounts: Flow<Map<String, SavedState.JellyseerrAccount>> =
    savedStateStorage
      .savedState
      .map { it.jellyseerrAccounts }
      .distinctUntilChanged()

  override val selectedJellyseerrAccount: Flow<SavedState.JellyseerrAccount?> = savedStateStorage
    .savedState
    .map {
      it.jellyseerrAccounts[it.selectedJellyseerrAccountId]
    }

  override suspend fun updateJellyseerrAccount(account: SavedState.JellyseerrAccount) {
    savedStateStorage.setJellyseerrAccount(account)
  }

  override suspend fun clearSelectedJellyseerrAccount() {
    savedStateStorage.clearSelectedJellyseerrAccount()
  }
}
