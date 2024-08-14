package com.divinelink.feature.settings.app.appearance.usecase.material.you

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ObserveMaterialYouModeUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Boolean>(dispatcher.default) {
  override fun execute(parameters: Unit): Flow<Result<Boolean>> =
    preferenceStorage.isMaterialYouEnabled.map {
      Result.success(it)
    }
}
