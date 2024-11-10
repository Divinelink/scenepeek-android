package com.divinelink.scenepeek.fakes.usecase.details

import com.divinelink.feature.details.media.usecase.SubmitRatingUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeSubmitRatingUseCase {

  val mock: SubmitRatingUseCase = mock()

  fun mockSubmitRate(response: Flow<Result<Unit>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
