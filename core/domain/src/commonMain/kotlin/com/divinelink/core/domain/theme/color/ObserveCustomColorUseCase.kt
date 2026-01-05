package com.divinelink.core.domain.theme.color

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCustomColorUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Long>(dispatcher.default) {
  override fun execute(parameters: Unit): Flow<Result<Long>> =
    preferenceStorage.customColor.map { preference ->
      Result.success(preference)
    }
}
