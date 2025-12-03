package com.divinelink.core.domain.theme

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.Theme

open class SetThemeUseCase(
  private val preferenceStorage: PreferenceStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Theme, Unit>(dispatcher.default) {
  override suspend fun execute(parameters: Theme) {
    preferenceStorage.selectTheme(parameters.storageKey)
  }
}
