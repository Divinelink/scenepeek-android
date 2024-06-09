package com.andreolas.movierama.settings.app.appearance.usecase.material.you

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.divinelink.core.commons.domain.UseCase
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
