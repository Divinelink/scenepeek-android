package com.andreolas.movierama.fakes.usecase.session.login

import com.andreolas.movierama.session.login.domain.session.usecase.ObserveSessionUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeObserveSessionUseCase {

  val mock: ObserveSessionUseCase = mock<ObserveSessionUseCase>()

  init {
    mockFailure()
  }

  fun mockFailure() {
    whenever(
      mock.invoke(any())
    ).thenReturn(
      flowOf(Result.failure(Exception()))
    )
  }

  fun mockSuccess(
    response: Result<Boolean>,
  ) {
    whenever(mock.invoke(any())).thenReturn(
      flowOf(response)
    )
  }
}
