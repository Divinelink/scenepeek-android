package com.divinelink.core.domain.theme.material.you

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage

class SetMaterialYouUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Boolean, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: Boolean) {
    preferenceStorage.setMaterialYou(parameters)
  }
}
