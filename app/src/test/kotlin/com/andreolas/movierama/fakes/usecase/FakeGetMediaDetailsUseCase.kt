package com.andreolas.movierama.fakes.usecase

import com.divinelink.feature.details.ui.MediaDetailsResult
import com.divinelink.feature.details.usecase.GetMediaDetailsUseCase
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
