package com.divinelink.core.data.preferences

import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.ui.UiSettingsStorage
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProdPreferencesRepository(
  private val storage: UiSettingsStorage,
  private val preferenceStorage: PreferenceStorage,
) : PreferencesRepository {
  override val uiPreferences
    get() = storage.uiPreferences

  override val themePreferences: Flow<ThemePreferences>
    get() = combine(
      preferenceStorage.selectedTheme,
      preferenceStorage.customColor,
      preferenceStorage.colorSystem,
      preferenceStorage.isBlackBackgroundsEnabled,
    ) { currentTheme, color, colorSystem, isPureBlack ->
      ThemePreferences(
        theme = Theme.from(currentTheme) ?: Theme.SYSTEM,
        colorSystem = colorSystem,
        themeColor = color,
        isPureBlack = isPureBlack,
      )
    }

  override suspend fun switchViewMode(section: ViewableSection) {
    storage.updateViewMode(
      section = section,
    )
  }

  override suspend fun switchSortDirection(section: ViewableSection) {
    storage.updateSortDirection(section)
  }

  override suspend fun switchSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  ) {
    storage.updateSortBy(section, sortBy)
  }

  override suspend fun updateCurrentTheme(theme: Theme) {
    preferenceStorage.selectTheme(theme.storageKey)
  }

  override suspend fun updateColorSystem(system: ColorSystem) {
    preferenceStorage.setColorSystem(system)
  }

  override suspend fun updateThemeColor(color: Long) {
    preferenceStorage.setThemeColor(color)
  }

  override suspend fun setPureBlack(enabled: Boolean) {
    preferenceStorage.setBlackBackgrounds(enabled)
  }
}
