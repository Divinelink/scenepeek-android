package com.divinelink.feature.settings.app.appearance.usecase.black.backgrounds

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetBlackBackgroundsUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Boolean, Unit>(dispatcher) {
  override suspend fun execute(parameters: Boolean) {
    preferenceStorage.setBlackBackgrounds(parameters)
  }
}
