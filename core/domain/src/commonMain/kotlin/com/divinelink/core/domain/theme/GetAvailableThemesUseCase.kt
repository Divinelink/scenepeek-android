package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.designsystem.theme.Theme

class GetAvailableThemesUseCase(
  private val systemThemeProvider: SystemThemeProvider,
  dispatcher: DispatcherProvider,
) :
  UseCase<Unit, List<Theme>>(dispatcher.default) {

  override suspend fun execute(parameters: Unit): List<Theme> = when {
    systemThemeProvider.isSystemAvailable() -> listOf(Theme.SYSTEM, Theme.LIGHT, Theme.DARK)
    else -> listOf(Theme.LIGHT, Theme.DARK)
  }
}
