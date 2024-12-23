package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.details.media.FetchAllRatingsUseCase
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFetchAllRatingsUseCase {

  val mock: FetchAllRatingsUseCase = mock()

  init {
    mockFailure()
  }

  fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(Exception())))
  }

  fun mockSuccess(response: Pair<RatingSource, RatingDetails>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.success(response)))
  }

  fun mockSuccess(response: Channel<Result<Pair<RatingSource, RatingDetails>>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}
