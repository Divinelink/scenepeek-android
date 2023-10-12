package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import gr.divinelink.core.util.domain.Result
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMarkAsFavoriteUseCase {

  val mock: MarkAsFavoriteUseCase = mock()

  suspend fun mockMarkAsFavoriteResult(
    result: Result<Unit>,
  ) {
    whenever(mock(any())).thenReturn(result)
  }
}
