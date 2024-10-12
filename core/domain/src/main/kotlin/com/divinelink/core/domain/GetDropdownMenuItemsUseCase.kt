package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.model.details.DetailsMenuOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDropdownMenuItemsUseCase(val dispatcher: DispatcherProvider) :
  FlowUseCase<Unit, List<DetailsMenuOptions>>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<List<DetailsMenuOptions>>> = flow {
    val menuItems = buildList {
      add(DetailsMenuOptions.SHARE)
    }
    Result.success(menuItems)
  }
}
