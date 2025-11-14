package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.FindByIdUseCase
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFindByIdUseCase {

  val mock: com.divinelink.core.domain.FindByIdUseCase = mock()

  init {
    mockFailure()
  }

  fun mockFailure(exception: Exception = Exception()) {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(exception)),
    )
  }

  fun mockSuccess(response: Result<MediaItem>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
