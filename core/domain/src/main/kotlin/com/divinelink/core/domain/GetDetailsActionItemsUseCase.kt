package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.details.DetailActionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetDetailsActionItemsUseCase(
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, List<DetailActionItem>>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<List<DetailActionItem>>> =
    storage.jellyseerrAccount.mapLatest { account ->
      val menuItems = buildList {
        addAll(DetailActionItem.defaultItems)
        account?.firstOrNull()?.let {
          add(DetailActionItem.Request)
        }
      }
      Result.success(menuItems)
    }
}
