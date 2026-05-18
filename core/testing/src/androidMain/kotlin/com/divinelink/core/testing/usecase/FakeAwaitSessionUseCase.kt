package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.session.AwaitSessionUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class FakeAwaitSessionUseCase {

  val mock: AwaitSessionUseCase = mock {
    onBlocking { invoke(any()) } doReturn Result.success(Unit)
  }
}
