package com.divinelink.core.domain.theme.black.backgrounds

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ObserveBlackBackgroundsUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcher.default) {
  override fun execute(parameters: Unit): Flow<Result<Boolean>> =
    preferenceStorage.isBlackBackgroundsEnabled.map {
      Result.success(it)
    }
}
