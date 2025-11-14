package com.divinelink.scenepeek.fakes.usecase.details

import com.divinelink.feature.details.media.usecase.DeleteRatingUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDeleteRatingUseCase {

  val mock: DeleteRatingUseCase = mock()

  suspend fun mockDeleteRating(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
