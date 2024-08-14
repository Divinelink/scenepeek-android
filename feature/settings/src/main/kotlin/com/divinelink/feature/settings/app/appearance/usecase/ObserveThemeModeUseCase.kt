package com.divinelink.feature.settings.app.appearance.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.designsystem.theme.themeFromStorageKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ObserveThemeModeUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : FlowUseCase<Unit, Theme>(dispatcher.default) {
  override fun execute(parameters: Unit): Flow<Result<Theme>> =
    preferenceStorage.selectedTheme.map {
      val theme = themeFromStorageKey(it) ?: Theme.SYSTEM
      Result.success(theme)
    }
}
