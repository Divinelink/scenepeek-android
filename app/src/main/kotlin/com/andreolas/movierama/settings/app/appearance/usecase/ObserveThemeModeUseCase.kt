package com.andreolas.movierama.settings.app.appearance.usecase

import com.andreolas.movierama.base.di.DefaultDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.designsystem.theme.themeFromStorageKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class ObserveThemeModeUseCase @Inject constructor(
  private val preferenceStorage: PreferenceStorage,
  @DefaultDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Theme>(dispatcher) {
  override fun execute(parameters: Unit): Flow<Result<Theme>> {
    return preferenceStorage.selectedTheme.map {
      val theme = themeFromStorageKey(it) ?: Theme.SYSTEM
      Result.success(theme)
    }
  }
}
