package com.divinelink.feature.settings.app.appearance.usecase.material.you

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMaterialYouUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Boolean>(dispatcher) {
  override suspend fun execute(parameters: Unit): Boolean {
    return preferenceStorage.isMaterialYouEnabled.first()
  }
}
