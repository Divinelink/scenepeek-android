package com.divinelink.feature.settings.app.appearance.usecase

import android.os.Build
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.designsystem.theme.Theme

class GetAvailableThemesUseCase(dispatcher: DispatcherProvider) :
  UseCase<Unit, List<Theme>>(dispatcher.main) {

  override suspend fun execute(parameters: Unit): List<Theme> = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
      listOf(Theme.SYSTEM, Theme.LIGHT, Theme.DARK)
    }
    else -> {
      listOf(Theme.LIGHT, Theme.DARK)
    }
  }
}
