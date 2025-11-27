package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.details.media.SubmitRatingUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeSubmitRatingUseCase {

  val mock: SubmitRatingUseCase = mock()

  suspend fun mockSubmitRate(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
