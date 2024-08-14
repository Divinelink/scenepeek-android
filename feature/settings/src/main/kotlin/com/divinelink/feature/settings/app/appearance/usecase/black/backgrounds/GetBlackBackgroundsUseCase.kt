package com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.first

class GetBlackBackgroundsUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, Boolean>(dispatcher.io) {
  override suspend fun execute(parameters: Unit): Boolean =
    preferenceStorage.isBlackBackgroundsEnabled.first()
}
