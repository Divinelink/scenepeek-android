package com.divinelink.core.testing.repository

import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.datastore.auth.SavedState
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class TestAuthRepository {
  val mock: AuthRepository = mock()

  fun mockJellyseerrEnabled(isEnabled: Boolean) {
    whenever(mock.isJellyseerrEnabled).thenReturn(flowOf(isEnabled))
  }

  fun mockJellyseerrAccounts(accounts: Map<String, SavedState.JellyseerrAccount>) {
    whenever(mock.jellyseerrAccounts).thenReturn(flowOf(accounts))
  }

  fun mockSelectedJellyseerrAccount(account: SavedState.JellyseerrAccount?) {
    whenever(mock.selectedJellyseerrAccount).thenReturn(flowOf(account))
  }

  suspend fun verifyAccountUpdated(account: SavedState.JellyseerrAccount) {
    verify(mock).updateJellyseerrAccount(account)
  }

  suspend fun verifyClearSelectedJellyseerrAccount() {
    verify(mock).clearSelectedJellyseerrAccount()
  }

  fun verifyNoInteractionsForUpdateJellyseerrAccount() {
    verifyNoInteractions(mock)
  }
}
