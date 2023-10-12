package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.RemoveFavoriteUseCase
import gr.divinelink.core.util.domain.Result
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeRemoveFavoriteUseCase {

  val mock: RemoveFavoriteUseCase = mock()

  suspend fun mockRemoveFavoriteResult(
    result: Result<Unit>,
  ) {
    whenever(mock(any())).thenReturn(result)
  }
}
