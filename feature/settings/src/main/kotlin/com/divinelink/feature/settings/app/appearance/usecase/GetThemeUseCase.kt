package com.divinelink.feature.settings.app.appearance.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.designsystem.theme.themeFromStorageKey
import kotlinx.coroutines.flow.first

class GetThemeUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, Theme>(dispatcher.io) {
  override suspend fun execute(parameters: Unit): Theme {
    val selectedTheme = preferenceStorage.selectedTheme.first()
    return themeFromStorageKey(selectedTheme) ?: Theme.SYSTEM
  }
}
