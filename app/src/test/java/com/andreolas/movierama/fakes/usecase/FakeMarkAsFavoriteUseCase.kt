package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class FakeMarkAsFavoriteUseCase : MarkAsFavoriteUseCase(
  repository = FakeMoviesRepository().mock,
  dispatcher = MainDispatcherRule().testDispatcher,
) {
  private var resultForMarkAsFavoriteMap: MutableMap<MediaItem.Media, Result<Unit>> = mutableMapOf()

  fun mockMarkAsFavoriteResult(
    media: MediaItem.Media,
    result: Result<Unit>,
  ) {
    resultForMarkAsFavoriteMap[media] = result
  }

  override suspend fun execute(parameters: MediaItem.Media) {
    return resultForMarkAsFavoriteMap[parameters]?.data!!
  }
}

/*
class FakeMarkAsFavoriteUseCase {

  val mock: MarkAsFavoriteUseCase = mock()

  suspend fun mockMarkAsFavoriteResult(
    result: Result<Unit>,
  ) {
    whenever(mock(any())).thenReturn(result)
  }
}
 */
