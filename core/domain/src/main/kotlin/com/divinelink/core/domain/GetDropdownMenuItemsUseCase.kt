package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDropdownMenuItemsUseCase(dispatcher: DispatcherProvider) :
  FlowUseCase<MediaType, List<DetailsMenuOptions>>(dispatcher.io) {

  override fun execute(parameters: MediaType): Flow<Result<List<DetailsMenuOptions>>> = flow {
    val menuItems = buildList {
      if (parameters == MediaType.TV) {
        add(DetailsMenuOptions.OBFUSCATE_SPOILERS)
      }
      add(DetailsMenuOptions.SHARE)
    }
    emit(Result.success(menuItems))
  }
}
