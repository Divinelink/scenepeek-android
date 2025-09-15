package com.divinelink.core.data.auth

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.isJellyseerrEnabled
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProdAuthRepository(private val savedStateStorage: SavedStateStorage) : AuthRepository {

  override val isJellyseerrEnabled: Flow<Boolean> = savedStateStorage
    .savedState
    .map { it.isJellyseerrEnabled() }

  override val jellyseerrCredentials: Flow<Map<String, SavedState.JellyseerrCredentials>> =
    savedStateStorage
      .savedState
      .map { it.jellyseerrCredentials }
      .distinctUntilChanged()

  override val selectedJellyseerrCredentials: Flow<SavedState.JellyseerrCredentials?> =
    savedStateStorage
      .savedState
      .map { it.jellyseerrCredentials[it.selectedJellyseerrId] }
      .distinctUntilChanged()

  override val selectedJellyseerrProfile: Flow<JellyseerrProfile?> = savedStateStorage
    .savedState
    .map { it.jellyseerrProfiles[it.selectedJellyseerrId] }
    .distinctUntilChanged()

  override suspend fun updateJellyseerrCredentials(account: SavedState.JellyseerrCredentials) {
    savedStateStorage.setJellyseerrCredentials(account)
  }

  override suspend fun updateJellyseerrProfile(profile: JellyseerrProfile) {
    savedStateStorage.setJellyseerrProfile(profile)
  }

  override suspend fun clearSelectedJellyseerrAccount() {
    savedStateStorage.clearSelectedJellyseerrAccount()
  }
}
