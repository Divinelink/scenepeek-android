package com.divinelink.core.domain.theme.color

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.model.ColorPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveColorPreferencesUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, ColorPreference>(dispatcher.default) {
  override fun execute(parameters: Unit): Flow<Result<ColorPreference>> =
    preferenceStorage.colorPreference.map { preference ->
      Result.success(preference)
    }
}
