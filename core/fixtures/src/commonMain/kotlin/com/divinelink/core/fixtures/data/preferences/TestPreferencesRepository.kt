package com.divinelink.core.fixtures.data.preferences

import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.home.HomeSectionsConfig
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.locale.AppLanguage
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.preferences.AppSettings
import com.divinelink.core.model.preferences.DetailPreferences
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.sort.other
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TestPreferencesRepository(
  uiPreferences: UiPreferences = UiPreferences.Initial,
  themePreferences: ThemePreferences = ThemePreferences.initial,
  detailPreferences: DetailPreferences = DetailPreferences.initial,
  appSettings: AppSettings = AppSettings.initial,
  homeSections: HomeSectionsConfig = HomeSectionsConfig.initial,
) : PreferencesRepository {

  private val _uiPreferences = MutableStateFlow(uiPreferences)
  override val uiPreferences: Flow<UiPreferences> = _uiPreferences

  private val _themePreferences = MutableStateFlow(themePreferences)
  override val themePreferences: Flow<ThemePreferences> = _themePreferences

  private val _detailPreferences = MutableStateFlow(detailPreferences)
  override val detailPreferences: Flow<DetailPreferences> = _detailPreferences

  private val _homeSections = MutableStateFlow(homeSections)
  override val homeSections: Flow<HomeSectionsConfig> = _homeSections

  private val _appSettings = MutableStateFlow(appSettings)
  override val appSettings: Flow<AppSettings> = _appSettings

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

    currentSortBy?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption +
          (section to option.copy(direction = option.direction.other())),
      )
    }
  }

  override suspend fun switchSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  ) {
    val currentSortBy = _uiPreferences.value.sortOption[section]

    currentSortBy?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption +
          (section to option.copy(sortBy = option.sortBy)),
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

  override suspend fun setAppLanguage(language: AppLanguage) {
    _themePreferences.value = _themePreferences.value.copy(
      appLanguage = language.localeName,
    )
  }

  override suspend fun setMetadataLanguage(language: Language) {
    _appSettings.value = _appSettings.value.copy(
      metadataLanguage = language,
    )
  }

  override fun listSortOption(listId: Int): Flow<SortOption?> = uiPreferences
    .map { it.listSortOptions[listId] }

  override suspend fun switchListSortBy(
    listId: Int,
    sortBy: SortBy,
  ) {
    val listSortOptions = _uiPreferences.value.listSortOptions[listId]

    listSortOptions?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        listSortOptions = _uiPreferences.value.listSortOptions +
          (listId to option.copy(sortBy = sortBy)),
      )
    }
  }

  override suspend fun switchListSortDirection(listId: Int) {
    val listSortOptions = _uiPreferences.value.listSortOptions[listId]

    listSortOptions?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        listSortOptions = _uiPreferences.value.listSortOptions +
          (listId to option.copy(direction = option.direction.other())),
      )
    }
  }

  override suspend fun clearListSortOption(listId: Int) {
    val listSortOptions = _uiPreferences.value.listSortOptions[listId]

    listSortOptions?.let {
      _uiPreferences.value = _uiPreferences.value.copy(
        listSortOptions = _uiPreferences.value.listSortOptions - listId,
      )
    }
  }

  override suspend fun toggleHomeSectionVisibility(section: MediaListSection.Home) {
    val hiddenSet = _homeSections.value.hidden

    _homeSections.value = _homeSections.value.copy(
      hidden = if (section in hiddenSet) {
        hiddenSet.minus(section)
      } else {
        hiddenSet.plus(section)
      },
    )
  }

  override suspend fun updateHomeSectionsOrder(sections: List<MediaListSection.Home>) {
    _homeSections.value = _homeSections.value.copy(
      order = sections,
    )
  }

  override suspend fun switchExpandedState(section: ExpandableSection) {
    _uiPreferences.value.expanded[section]?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        expanded = _uiPreferences.value.expanded + (section to !option),
      )
    }
  }
}
