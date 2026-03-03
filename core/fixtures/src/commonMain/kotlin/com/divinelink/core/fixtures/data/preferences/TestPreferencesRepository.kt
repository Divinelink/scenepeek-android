package com.divinelink.core.fixtures.data.preferences

import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.preferences.DetailPreferences
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.other
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestPreferencesRepository(
  uiPreferences: UiPreferences = UiPreferences.Initial,
  themePreferences: ThemePreferences = ThemePreferences.initial,
  detailPreferences: DetailPreferences = DetailPreferences.initial,
) : PreferencesRepository {

  private val _uiPreferences = MutableStateFlow(uiPreferences)
  override val uiPreferences: Flow<UiPreferences> = _uiPreferences

  private val _themePreferences = MutableStateFlow(themePreferences)
  override val themePreferences: Flow<ThemePreferences> = _themePreferences

  private val _detailPreferences = MutableStateFlow(detailPreferences)
  override val detailPreferences: Flow<DetailPreferences> = _detailPreferences

  override suspend fun switchViewMode(section: ViewableSection) {
    val currentViewMode = _uiPreferences.value.viewModes[section]

    currentViewMode?.let { viewMode ->
      _uiPreferences.value = _uiPreferences.value.copy(
        viewModes = _uiPreferences.value.viewModes + (section to viewMode.other()),
      )
    }
  }

  override suspend fun switchSortDirection(section: ViewableSection) {
    val currentSortBy = _uiPreferences.value.sortOption[section]

    currentSortBy?.let { sortBy ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption + (
          section to sortBy.copy(direction = sortBy.direction.other())
          ),
      )
    }
  }

  override suspend fun switchSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  ) {
    val currentSortBy = _uiPreferences.value.sortOption[section]

    currentSortBy?.let { sortOption ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption + (
          section to sortOption.copy(sortBy = sortOption.sortBy)
          ),
      )
    }
  }

  override suspend fun updateCurrentTheme(theme: Theme) {
    _themePreferences.value = _themePreferences.value.copy(
      theme = theme,
    )
  }

  override suspend fun updateColorSystem(system: ColorSystem) {
    _themePreferences.value = _themePreferences.value.copy(
      colorSystem = system,
    )
  }

  override suspend fun updateThemeColor(color: Long) {
    _themePreferences.value = _themePreferences.value.copy(
      themeColor = color,
    )
  }

  override suspend fun setPureBlack(enabled: Boolean) {
    _themePreferences.value = _themePreferences.value.copy(
      isPureBlack = enabled,
    )
  }

  override suspend fun setRegion(country: Country) {
    _detailPreferences.value = _detailPreferences.value.copy(
      region = country,
    )
  }

  override suspend fun setStreamingServicesVisible(visible: Boolean) {
    _detailPreferences.value = _detailPreferences.value.copy(
      streamingServicesVisible = visible,
    )
  }
}
