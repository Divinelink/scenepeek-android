package com.divinelink.core.fixtures.data.preferences

import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestPreferencesRepository(
  uiPreferences: UiPreferences = UiPreferences.Initial,

) :
  PreferencesRepository {

  private val uiPreferencesFlow = MutableStateFlow(uiPreferences)

  override val uiPreferences: Flow<UiPreferences> = uiPreferencesFlow

  override val themePreferences: StateFlow<ThemePreferences>
    get() = TODO("Not yet implemented")

  override suspend fun switchViewMode(section: ViewableSection) {
    val currentViewMode = uiPreferencesFlow.value.viewModes[section]

    currentViewMode?.let { viewMode ->
      uiPreferencesFlow.value = uiPreferencesFlow.value.copy(
        viewModes = uiPreferencesFlow.value.viewModes + (section to viewMode.other()),
      )
    }
  }

  override suspend fun updateCurrentTheme(theme: Theme) {
    TODO("Not yet implemented")
  }

  override suspend fun updateColorSystem(system: ColorSystem) {
    TODO("Not yet implemented")
  }

  override suspend fun updateThemeColor(color: Long) {
    TODO("Not yet implemented")
  }

  override suspend fun setPureBlack(enabled: Boolean) {
    TODO("Not yet implemented")
  }
}
