package com.divinelink.core.domain

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.details.DetailsMenuOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetDropdownMenuItemsUseCase @Inject constructor(
  private val storage: PreferenceStorage,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<DetailsMenuOptions>>(dispatcher) {

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
