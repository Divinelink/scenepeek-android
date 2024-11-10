package com.divinelink.scenepeek.fakes.usecase

import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.scenepeek.fakes.repository.FakeMoviesRepository

class FakeMarkAsFavoriteUseCase :
  MarkAsFavoriteUseCase(
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

  override suspend fun execute(parameters: MediaItem.Media) =
    resultForMarkAsFavoriteMap[parameters]?.data!!
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
