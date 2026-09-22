package com.divinelink.core.data.preferences

import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.ui.UiSettingsStorage
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
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

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
      preferenceStorage.appLanguage,
    ) { currentTheme, color, colorSystem, isPureBlack, language ->
      ThemePreferences(
        theme = Theme.from(currentTheme) ?: Theme.SYSTEM,
        colorSystem = colorSystem,
        themeColor = color,
        isPureBlack = isPureBlack,
        appLanguage = language.localeName,
      )
    }

  override val detailPreferences: Flow<DetailPreferences>
    get() = preferenceStorage
      .appSettings
      .map { settings ->
        DetailPreferences(
          region = settings.region,
          streamingServicesVisible = settings.visibleStreamingServices,
        )
      }

  override val homeSections: Flow<HomeSectionsConfig>
    get() = preferenceStorage.homeSections

  override val appSettings: Flow<AppSettings>
    get() = preferenceStorage.appSettings

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

  override suspend fun clearListSortOption(listId: Int) {
    storage.clearListSortOption(listId)
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

  override suspend fun setRegion(country: Country) {
    preferenceStorage.setRegion(country)
  }

  override suspend fun setStreamingServicesVisible(visible: Boolean) {
    preferenceStorage.setStreamingServicesVisible(visible)
  }

  override suspend fun setAppLanguage(language: AppLanguage) {
    preferenceStorage.setAppLanguage(language)
  }

  override suspend fun setMetadataLanguage(language: Language) {
    preferenceStorage.setMetadataLanguage(language)
  }

  override suspend fun switchListSortBy(
    listId: Int,
    sortBy: SortBy,
  ) {
    storage.updateListSortBy(listId, sortBy)
  }

  override suspend fun switchListSortDirection(listId: Int) {
    storage.updateListSortDirection(listId)
  }

  override fun listSortOption(listId: Int): Flow<SortOption?> = storage.listSortOption(listId)

  override suspend fun toggleHomeSectionVisibility(section: MediaListSection.Home) {
    preferenceStorage.toggleHomeSectionVisibility(section)
  }

  override suspend fun updateHomeSectionsOrder(sections: List<MediaListSection.Home>) {
    preferenceStorage.updateHomeSectionsOrder(sections)
  }

  override suspend fun switchExpandedState(section: ExpandableSection) {
    storage.expandSection(section)
  }
}
