package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.model.details.DetailActionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetDetailsActionItemsUseCase(val dispatcher: DispatcherProvider) :
  FlowUseCase<Unit, List<DetailActionItem>>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<List<DetailActionItem>>> =
    flowOf(Result.success(DetailActionItem.defaultItems))
}
