package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.media.MediaType

class GetDetailsActionItemsUseCase(
  val dispatcher: DispatcherProvider,
) : UseCase<MediaType, List<DetailActionItem>>(dispatcher.default) {

  override suspend fun execute(parameters: MediaType): List<DetailActionItem> = when (parameters) {
    MediaType.TV -> DetailActionItem.defaultTvItems
    MediaType.MOVIE -> DetailActionItem.defaultItems
    MediaType.PERSON -> emptyList()
    MediaType.UNKNOWN -> emptyList()
  }
}
