package com.divinelink.core.domain.theme.material.you

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.first

class GetMaterialYouUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, Boolean>(dispatcher.default) {
  override suspend fun execute(parameters: Unit): Boolean =
    preferenceStorage.isMaterialYouEnabled.first()
}
