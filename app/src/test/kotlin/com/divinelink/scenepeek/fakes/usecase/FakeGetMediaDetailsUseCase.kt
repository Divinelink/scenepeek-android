package com.divinelink.scenepeek.fakes.usecase

import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.feature.details.media.usecase.GetMediaDetailsUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetMediaDetailsUseCase {

  val mock: GetMediaDetailsUseCase = mock()

  fun mockFetchMediaDetails(response: Flow<Result<MediaDetailsResult>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
