package com.andreolas.movierama.settings.app.appearance.usecase.black.backgrounds

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import gr.divinelink.core.util.domain.UseCase
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