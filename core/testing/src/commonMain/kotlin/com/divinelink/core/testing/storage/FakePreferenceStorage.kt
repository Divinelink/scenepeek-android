package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.seedLong
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.home.HomeSectionsConfig
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.locale.AppLanguage
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.preferences.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferenceStorage(
  selectedTheme: String = "",
  isBlackBackgroundsEnabled: Boolean = false,
  spoilersObfuscation: Boolean = false,
  movieRatingSource: RatingSource = RatingSource.TMDB,
  tvRatingSource: RatingSource = RatingSource.TMDB,
  episodesRatingSource: RatingSource = RatingSource.TMDB,
  seasonsRatingSource: RatingSource = RatingSource.TMDB,
  colorSystem: ColorSystem = ColorSystem.Dynamic,
  customColor: Long = seedLong,
  appLanguage: AppLanguage = AppLanguage.DEFAULT,
  homeSections: HomeSectionsConfig = HomeSectionsConfig.initial,
  appSettings: AppSettings = AppSettings.initial,
) : PreferenceStorage {

  private val _selectedTheme = MutableStateFlow(selectedTheme)
  override val selectedTheme = _selectedTheme

  private val _colorSystem = MutableStateFlow(colorSystem)
  override val colorSystem = _colorSystem

  private val _customColor = MutableStateFlow(customColor)
  override val customColor = _customColor

  private val _isBlackBackgroundsEnabled = MutableStateFlow(isBlackBackgroundsEnabled)
  override val isBlackBackgroundsEnabled = _isBlackBackgroundsEnabled

  private val _spoilersObfuscation = MutableStateFlow(spoilersObfuscation)
  override val spoilersObfuscation: Flow<Boolean> = _spoilersObfuscation

  private val _movieRatingSource = MutableStateFlow(movieRatingSource)
  override val movieRatingSource: Flow<RatingSource> = _movieRatingSource

  private val _tvRatingSource = MutableStateFlow(tvRatingSource)
  override val tvRatingSource: Flow<RatingSource> = _tvRatingSource

  private val _episodesRatingSource = MutableStateFlow(episodesRatingSource)
  override val episodesRatingSource: Flow<RatingSource> = _episodesRatingSource

  private val _seasonsRatingSource = MutableStateFlow(seasonsRatingSource)
  override val seasonsRatingSource: Flow<RatingSource> = _seasonsRatingSource

  private val _appLanguage = MutableStateFlow(appLanguage)
  override val appLanguage: Flow<AppLanguage> = _appLanguage

  private val _appSettings = MutableStateFlow(appSettings)
  override val appSettings: Flow<AppSettings> = _appSettings

  private val _homeSections = MutableStateFlow(homeSections)
  override val homeSections: Flow<HomeSectionsConfig> = _homeSections

  override suspend fun selectTheme(theme: String) {
    _selectedTheme.value = theme
  }

  override suspend fun setBlackBackgrounds(isEnabled: Boolean) {
    _isBlackBackgroundsEnabled.value = isEnabled
  }

  override suspend fun setSpoilersObfuscation(isEnabled: Boolean) {
    _spoilersObfuscation.value = isEnabled
  }

  override suspend fun setMovieRatingSource(ratingSource: RatingSource) {
    _movieRatingSource.value = ratingSource
  }

  override suspend fun setTvRatingSource(ratingSource: RatingSource) {
    _tvRatingSource.value = ratingSource
  }

  override suspend fun setEpisodesRatingSource(ratingSource: RatingSource) {
    _episodesRatingSource.value = ratingSource
  }

  override suspend fun setSeasonsRatingSource(ratingSource: RatingSource) {
    _seasonsRatingSource.value = ratingSource
  }

  override suspend fun setColorSystem(preference: ColorSystem) {
    _colorSystem.value = preference
  }

  override suspend fun setThemeColor(color: Long) {
    _customColor.value = color
  }

  override suspend fun setRegion(country: Country) {
    _appSettings.value = _appSettings.value.copy(region = country)
  }

  override suspend fun setStreamingServicesVisible(visible: Boolean) {
    _appSettings.value = _appSettings.value.copy(visibleStreamingServices = visible)
  }

  override suspend fun setUpdaterOptIn(checkForUpdates: Boolean) {
    _appSettings.value = _appSettings.value.copy(inAppUpdaterOptIn = checkForUpdates)
  }

  override suspend fun setAppLanguage(language: AppLanguage) {
    _appLanguage.value = language
  }

  override suspend fun setMetadataLanguage(language: Language) {
    _appSettings.value = _appSettings.value.copy(metadataLanguage = language)
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
}
