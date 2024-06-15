package com.andreolas.movierama.settings.app.appearance.usecase.material.you

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetMaterialYouUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Boolean, Unit>(dispatcher) {
  override suspend fun execute(parameters: Boolean) {
    preferenceStorage.setMaterialYou(parameters)
  }
}
