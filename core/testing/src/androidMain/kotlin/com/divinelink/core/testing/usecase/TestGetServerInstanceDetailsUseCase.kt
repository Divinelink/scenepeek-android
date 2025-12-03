package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.GetServerInstanceDetailsUseCase
import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestGetServerInstanceDetailsUseCase {

  val mock: GetServerInstanceDetailsUseCase = mock()

  suspend fun mockFailure(exception: Exception) {
    whenever(mock.invoke(any())).thenReturn(Result.failure(exception))
  }

  suspend fun mockResponse(response: Result<ServerInstanceDetails>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
