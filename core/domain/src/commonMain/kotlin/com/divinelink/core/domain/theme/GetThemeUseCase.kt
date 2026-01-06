package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.model.Theme
import kotlinx.coroutines.flow.first

class GetThemeUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, Theme>(dispatcher.default) {
  override suspend fun execute(parameters: Unit): Theme {
    val selectedTheme = preferenceStorage.selectedTheme.first()
    return Theme.from(selectedTheme) ?: Theme.SYSTEM
  }
}
