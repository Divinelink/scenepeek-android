package com.andreolas.movierama.settings.app.appearance.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.designsystem.theme.Theme
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class SetThemeUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Theme, Unit>(dispatcher) {
  override suspend fun execute(parameters: Theme) {
    preferenceStorage.selectTheme(parameters.storageKey)
  }
}
