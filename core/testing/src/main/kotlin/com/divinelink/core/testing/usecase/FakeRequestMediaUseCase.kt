package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeRequestMediaUseCase {

  val mock: RequestMediaUseCase = mock()

  fun mockSuccess(response: Flow<Result<JellyseerrMediaRequest>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  fun mockFailure(throwable: Throwable) {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(throwable)),
    )
  }
}
