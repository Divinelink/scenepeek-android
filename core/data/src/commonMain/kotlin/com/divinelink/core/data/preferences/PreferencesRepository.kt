package com.divinelink.core.data.preferences

import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

  val uiPreferences: Flow<UiPreferences>

  val themePreferences: Flow<ThemePreferences>

  suspend fun switchViewMode(section: ViewableSection)
  suspend fun switchSortDirection(section: ViewableSection)
  suspend fun switchSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  )

  suspend fun updateCurrentTheme(theme: Theme)
  suspend fun updateColorSystem(system: ColorSystem)
  suspend fun updateThemeColor(color: Long)
  suspend fun setPureBlack(enabled: Boolean)
}
