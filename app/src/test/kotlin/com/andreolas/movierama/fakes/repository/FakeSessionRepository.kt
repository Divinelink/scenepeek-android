package com.andreolas.movierama.fakes.repository

import com.divinelink.core.data.session.repository.SessionRepository
import com.divinelink.core.model.account.AccountDetails
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeSessionRepository {

  val mock: SessionRepository = mock()

  fun mockGetAccountDetails(
    response: Result<AccountDetails>
  ) {
    whenever(
      mock.getAccountDetails(any())
    ).thenReturn(flowOf(response))
  }
}
