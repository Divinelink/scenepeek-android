package com.divinelink.feature.settings.app.appearance.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.designsystem.theme.themeFromStorageKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Theme>(dispatcher) {
  override suspend fun execute(parameters: Unit): Theme {
    val selectedTheme = preferenceStorage.selectedTheme.first()
    return themeFromStorageKey(selectedTheme)
      ?: Theme.SYSTEM
  }
}
