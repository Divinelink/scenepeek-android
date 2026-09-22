package com.divinelink.core.data.preferences

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
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

  val uiPreferences: Flow<UiPreferences>

  val themePreferences: Flow<ThemePreferences>

  val appSettings: Flow<AppSettings>

  val detailPreferences: Flow<DetailPreferences>

  val homeSections: Flow<HomeSectionsConfig>

  fun listSortOption(listId: Int): Flow<SortOption?>

  suspend fun switchExpandedState(section: ExpandableSection)
  suspend fun switchViewMode(section: ViewableSection)
  suspend fun switchSortDirection(section: ViewableSection)
  suspend fun switchSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  )

  suspend fun clearListSortOption(listId: Int)
  suspend fun switchListSortDirection(listId: Int)
  suspend fun switchListSortBy(
    listId: Int,
    sortBy: SortBy,
  )

  suspend fun updateCurrentTheme(theme: Theme)
  suspend fun updateColorSystem(system: ColorSystem)
  suspend fun updateThemeColor(color: Long)
  suspend fun setPureBlack(enabled: Boolean)
  suspend fun setAppLanguage(language: AppLanguage)
  suspend fun setMetadataLanguage(language: Language)

  suspend fun setRegion(country: Country)
  suspend fun setStreamingServicesVisible(visible: Boolean)

  suspend fun toggleHomeSectionVisibility(section: MediaListSection.Home)
  suspend fun updateHomeSectionsOrder(sections: List<MediaListSection.Home>)
}
