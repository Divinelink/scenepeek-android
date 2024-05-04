package com.andreolas.movierama.settings.app.appearance.usecase

import android.os.Build
import com.andreolas.movierama.base.di.MainDispatcher
import com.andreolas.movierama.ui.theme.Theme
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetAvailableThemesUseCase @Inject constructor(
  @MainDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, List<Theme>>(dispatcher) {

  override suspend fun execute(parameters: Unit): List<Theme> = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
      listOf(Theme.SYSTEM, Theme.LIGHT, Theme.DARK)
    }
    else -> {
      listOf(Theme.LIGHT, Theme.DARK)
    }
  }
}
