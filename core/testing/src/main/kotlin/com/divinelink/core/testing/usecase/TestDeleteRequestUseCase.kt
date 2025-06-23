package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.DeleteRequestUseCase
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestDeleteRequestUseCase {

  val mock: DeleteRequestUseCase = mock()

  init {
    mockFailure()
  }

  fun mockFailure(error: Throwable = Exception()) {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(error)))
  }

  fun mockSuccess(response: Flow<Result<JellyseerrMediaInfo>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
