package com.divinelink.core.testing.usecase

import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestMediaRepository

class TestMarkAsFavoriteUseCase :
  MarkAsFavoriteUseCase(
    repository = TestMediaRepository().mock,
    dispatcher = MainDispatcherRule().testDispatcher,
  ) {
  private var resultForMarkAsFavoriteMap: MutableMap<MediaItem.Media, Result<Boolean>> =
    mutableMapOf()

  fun mockMarkAsFavoriteResult(
    media: MediaItem.Media,
    result: Result<Boolean>,
  ) {
    resultForMarkAsFavoriteMap[media] = result
  }

  override suspend fun execute(parameters: MediaItem.Media): Boolean =
    resultForMarkAsFavoriteMap[parameters]?.data == true
}
