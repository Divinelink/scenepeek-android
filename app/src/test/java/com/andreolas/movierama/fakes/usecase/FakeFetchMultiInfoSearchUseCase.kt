package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.FetchMultiInfoSearchUseCase
import com.andreolas.movierama.home.domain.usecase.MultiSearchResult
import gr.divinelink.core.util.domain.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class FakeFetchMultiInfoSearchUseCase {

  val mock: FetchMultiInfoSearchUseCase = mockk()

  fun mockFetchMultiInfoSearch(
    response: Result<MultiSearchResult>,
  ) {
    coEvery {
      mock.invoke(any())
    } returns flowOf(response)
  }
}
