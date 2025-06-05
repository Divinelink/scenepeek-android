package com.divinelink.core.testing.usecase

import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestMoviesRepository

class TestMarkAsFavoriteUseCase :
  MarkAsFavoriteUseCase(
    repository = TestMoviesRepository().mock,
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
