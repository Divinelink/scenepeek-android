package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.GetServerInstancesUseCase
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestGetServerInstancesUseCase {

  val mock: GetServerInstancesUseCase = mock()

  suspend fun mockFailure(exception: Exception) {
    whenever(mock.invoke(any())).thenReturn(Result.failure(exception))
  }

  suspend fun mockResponse(response: Result<List<ServerInstance>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
