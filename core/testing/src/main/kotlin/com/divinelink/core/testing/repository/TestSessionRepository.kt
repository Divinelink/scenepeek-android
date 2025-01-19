package com.divinelink.core.testing.repository

import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.Session
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestSessionRepository {

  val mock: SessionRepository = mock()

  fun mockGetAccountDetails(response: Result<AccountDetails>) {
    whenever(
      mock.getAccountDetails(any()),
    ).thenReturn(flowOf(response))
  }

  suspend fun mockCreateSession(response: Result<Session>) {
    whenever(mock.createSession(any())).thenReturn(response)
  }
}
