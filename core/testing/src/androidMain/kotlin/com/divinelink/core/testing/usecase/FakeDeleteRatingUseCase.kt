package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.details.media.DeleteRatingUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDeleteRatingUseCase {

  val mock: DeleteRatingUseCase = mock()

  suspend fun mockDeleteRating(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
