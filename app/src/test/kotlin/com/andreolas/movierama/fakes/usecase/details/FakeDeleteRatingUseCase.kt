package com.andreolas.movierama.fakes.usecase.details

import com.divinelink.feature.details.usecase.DeleteRatingUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDeleteRatingUseCase {

  val mock: DeleteRatingUseCase = mock()

  fun mockDeleteRating(response: Flow<Result<Unit>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
