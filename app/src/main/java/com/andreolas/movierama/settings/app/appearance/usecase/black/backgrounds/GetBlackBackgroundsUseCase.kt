package com.andreolas.movierama.settings.app.appearance.usecase.black.backgrounds

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetBlackBackgroundsUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Boolean>(dispatcher) {
  override suspend fun execute(parameters: Unit): Boolean {
    return preferenceStorage.isBlackBackgroundsEnabled.first()
  }
}
