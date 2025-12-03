package com.divinelink.core.domain.theme.black.backgrounds

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage

class SetBlackBackgroundsUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Boolean, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: Boolean) {
    preferenceStorage.setBlackBackgrounds(parameters)
  }
}
