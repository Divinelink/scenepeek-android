package com.divinelink.core.testing.repository

import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.model.session.Session
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TestSessionRepository {

  val mock: SessionRepository = mock()

  suspend fun mockGetAccountDetails(response: Result<AccountDetails>) {
    whenever(
      mock.getAccountDetails(any()),
    ).thenReturn(response)
  }

  suspend fun mockCreateSession(response: Result<Session>) {
    whenever(mock.createSession(any())).thenReturn(response)
  }

  suspend fun mockRetrieveRequestToken(response: Result<RequestToken>) {
    whenever(mock.retrieveRequestToken()).thenReturn(response)
  }

  suspend fun mockCreateAccessToken(response: Result<AccessToken>) {
    whenever(mock.createAccessToken(any())).thenReturn(response)
  }

  suspend fun clearRequestTokenInvoke() {
    verify(mock).clearRequestToken()
  }
}
