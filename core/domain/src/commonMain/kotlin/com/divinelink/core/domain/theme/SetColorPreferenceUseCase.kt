package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.model.ColorSystem

class SetColorPreferenceUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<ColorSystem, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: ColorSystem) {
    preferenceStorage.setColorSystem(parameters)
  }
}
