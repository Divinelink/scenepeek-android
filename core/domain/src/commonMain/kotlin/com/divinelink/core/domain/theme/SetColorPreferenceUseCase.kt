package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.model.ColorPreference

class SetColorPreferenceUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<ColorPreference, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: ColorPreference) {
    preferenceStorage.setColorPreference(parameters)
  }
}
