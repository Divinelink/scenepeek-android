package com.divinelink.feature.settings.app.appearance.usecase

import com.divinelink.core.commons.di.DefaultDispatcher
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.datastore.PreferenceStorage
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
