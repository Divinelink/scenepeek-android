package com.divinelink.scenepeek.fakes.usecase.details

import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.feature.details.media.usecase.FetchAccountMediaDetailsUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeFetchAccountMediaDetailsUseCase {

  val mock: FetchAccountMediaDetailsUseCase = mock()

  fun mockFetchAccountDetails(response: Flow<Result<AccountMediaDetails>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
