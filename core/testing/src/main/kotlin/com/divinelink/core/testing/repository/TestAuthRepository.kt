package com.divinelink.core.testing.repository

import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.ProfilePermission
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TestAuthRepository {
  val mock: AuthRepository = mock()

  init {
    mockPermissions(flowOf(ProfilePermission.entries))
  }

  fun mockJellyseerrEnabled(isEnabled: Boolean) {
    whenever(mock.isJellyseerrEnabled).thenReturn(flowOf(isEnabled))
  }

  fun mockJellyseerrCredentials(accounts: Map<String, SavedState.JellyseerrCredentials>) {
    whenever(mock.jellyseerrCredentials).thenReturn(flowOf(accounts))
  }

  fun mockPermissions(permissions: Flow<List<ProfilePermission>>) {
    whenever(mock.profilePermissions).thenReturn(permissions)
  }

  fun mockPermissions(permissions: Channel<List<ProfilePermission>>) {
    whenever(mock.profilePermissions).thenReturn(permissions.consumeAsFlow())
  }

  fun mockSelectedJellyseerrCredentials(credentials: SavedState.JellyseerrCredentials?) {
    whenever(mock.selectedJellyseerrCredentials).thenReturn(flowOf(credentials))
  }

  fun mockSelectedJellyseerrProfile(profile: Flow<JellyseerrProfile?>) {
    whenever(mock.selectedJellyseerrProfile).thenReturn(profile)
  }

  suspend fun verifyAccountUpdated(account: SavedState.JellyseerrCredentials) {
    verify(mock).updateJellyseerrCredentials(account)
  }

  suspend fun verifyClearSelectedJellyseerrAccount() {
    verify(mock).clearSelectedJellyseerrAccount()
  }
}
