package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.DeleteMediaUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestDeleteMediaUseCase {

  val mock: DeleteMediaUseCase = mock()

  suspend fun mockFailure(error: Throwable = Exception()) {
    whenever(mock.invoke(any())).thenReturn((Result.failure(error)))
  }

  suspend fun mockSuccess(response: Result<Unit>) {
    whenever(mock(any())).thenReturn(response)
  }
}
