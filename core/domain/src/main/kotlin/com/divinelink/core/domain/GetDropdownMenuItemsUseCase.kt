package com.divinelink.core.domain


import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDropdownMenuItemsUseCase(
  private val storage: PreferenceStorage,
  val dispatcher: DispatcherProvider
) : FlowUseCase<Unit, List<DetailsMenuOptions>>(dispatcher.io) {

  override fun execute(parameters: Unit): Flow<Result<List<DetailsMenuOptions>>> = combine(
    storage.jellyseerrAccount,
  ) { account ->
    val menuItems = mutableListOf<DetailsMenuOptions>()
    menuItems.add(DetailsMenuOptions.SHARE)

    account.firstOrNull()?.let {
      menuItems.add(DetailsMenuOptions.REQUEST)
    }

    Result.success(menuItems)
  }
}
