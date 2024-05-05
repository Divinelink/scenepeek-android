package com.andreolas.movierama.fakes.usecase.details

import com.andreolas.movierama.details.domain.model.account.AccountMediaDetails
import com.andreolas.movierama.details.domain.usecase.FetchAccountMediaDetailsUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeFetchAccountMediaDetailsUseCase {

  val mock: FetchAccountMediaDetailsUseCase = mock()

  fun mockFetchAccountDetails(
    response: Flow<Result<AccountMediaDetails>>,
  ) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
