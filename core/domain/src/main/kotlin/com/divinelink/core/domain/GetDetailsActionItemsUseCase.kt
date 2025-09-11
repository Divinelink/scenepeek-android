package com.divinelink.core.domain

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.model.details.DetailActionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetDetailsActionItemsUseCase(
  private val authRepository: AuthRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, List<DetailActionItem>>(dispatcher.default) {

  override fun execute(parameters: Unit): Flow<Result<List<DetailActionItem>>> =
    authRepository.isJellyseerrEnabled.mapLatest { isJellyseerrEnabled ->
      val menuItems = buildList {
        addAll(DetailActionItem.defaultItems)
        if (isJellyseerrEnabled) {
          add(DetailActionItem.Request)
        }
      }
      Result.success(menuItems)
    }
}
