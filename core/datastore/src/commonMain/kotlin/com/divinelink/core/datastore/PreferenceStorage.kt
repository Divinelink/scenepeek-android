package com.divinelink.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.AUTOMATIC_BACKUPS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.BACKUP_PATH
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_APP_LANGUAGE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_BLACK_BACKGROUNDS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_COLOR_PREFERENCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_CUSTOM_COLOR_ID
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_EPISODES_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_METADATA_LANGUAGE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_MOVIE_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_REGION_CODE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SEASONS_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SERIES_TOTAL_EPISODES_OBFUSCATION
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_STREAMING_SERVICES_VISIBLE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_TV_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_UPDATER_OPT_IN
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.homeSectionDisplayOrderKey
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.homeSectionHiddenKey
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.seedLong
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.home.HomeSectionsConfig
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.locale.AppLanguage
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.preferences.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

interface PreferenceStorage {
  suspend fun selectTheme(theme: String)
  val selectedTheme: Flow<String>

  suspend fun setColorSystem(preference: ColorSystem)
  val colorSystem: Flow<ColorSystem>

  suspend fun setThemeColor(color: Long)
  val customColor: Flow<Long>

  suspend fun setBlackBackgrounds(isEnabled: Boolean)
  val isBlackBackgroundsEnabled: Flow<Boolean>

  suspend fun setSpoilersObfuscation(isEnabled: Boolean)
  val spoilersObfuscation: Flow<Boolean>

  suspend fun setMovieRatingSource(ratingSource: RatingSource)
  val movieRatingSource: Flow<RatingSource>

  suspend fun setTvRatingSource(ratingSource: RatingSource)
  val tvRatingSource: Flow<RatingSource>

  suspend fun setEpisodesRatingSource(ratingSource: RatingSource)
  val episodesRatingSource: Flow<RatingSource>

  suspend fun setSeasonsRatingSource(ratingSource: RatingSource)
  val seasonsRatingSource: Flow<RatingSource>

  suspend fun setRegion(country: Country)
  suspend fun setStreamingServicesVisible(visible: Boolean)
  suspend fun setUpdaterOptIn(checkForUpdates: Boolean)
  suspend fun setMetadataLanguage(language: Language)
  suspend fun setAppLanguage(language: AppLanguage)
  suspend fun setBackupPath(path: String)
  suspend fun setAutomaticBackups(enabled: Boolean)
  val appLanguage: Flow<AppLanguage>

  suspend fun toggleHomeSectionVisibility(section: MediaListSection.Home)
  suspend fun updateHomeSectionsOrder(sections: List<MediaListSection.Home>)
  val homeSections: Flow<HomeSectionsConfig>

  val appSettings: Flow<AppSettings>
}

class DataStorePreferenceStorage(private val dataStore: DataStore<Preferences>) :
  PreferenceStorage {

  companion object {
    const val PREFS_NAME = "application_preferences"
  }

  object PreferencesKeys {
    val PREF_SELECTED_THEME = stringPreferencesKey("settings.theme")
    val PREF_BLACK_BACKGROUNDS = booleanPreferencesKey("settings.black.backgrounds")
    val PREF_COLOR_PREFERENCE = stringPreferencesKey("settings.color.preference")
    val PREF_CUSTOM_COLOR_ID = longPreferencesKey("settings.custom.color")

    val PREF_SERIES_TOTAL_EPISODES_OBFUSCATION = booleanPreferencesKey(
      "settings.series.episode.obfuscation",
    )

    val PREF_APP_LANGUAGE = stringPreferencesKey("app.language")
    val PREF_METADATA_LANGUAGE = stringPreferencesKey("metadata.language")
    val PREF_MOVIE_RATING_SOURCE = stringPreferencesKey("movie.rating.source")
    val PREF_TV_RATING_SOURCE = stringPreferencesKey("tv.rating.source")
    val PREF_EPISODES_RATING_SOURCE = stringPreferencesKey("episodes.rating.source")
    val PREF_SEASONS_RATING_SOURCE = stringPreferencesKey("seasons.rating.source")
    val PREF_REGION_CODE = stringPreferencesKey("details.region.code")
    val PREF_STREAMING_SERVICES_VISIBLE = booleanPreferencesKey("streaming.services.visible")

    val PREF_UPDATER_OPT_IN = booleanPreferencesKey("updater.opt.in")

    val homeSectionHiddenKey = stringSetPreferencesKey("home_section_hidden")
    val homeSectionDisplayOrderKey = stringPreferencesKey("home_sections_display_order")

    val AUTOMATIC_BACKUPS = booleanPreferencesKey("automatic_backups")
    val BACKUP_PATH = stringPreferencesKey("backup_path")
  }

  override val appSettings: Flow<AppSettings> = dataStore.data.map { preferences ->
    AppSettings(
      automaticBackups = preferences[AUTOMATIC_BACKUPS] ?: false,
      backupPath = preferences[BACKUP_PATH],
      visibleStreamingServices = preferences[PREF_STREAMING_SERVICES_VISIBLE] ?: true,
      inAppUpdaterOptIn = preferences[PREF_UPDATER_OPT_IN] ?: true,
      metadataLanguage = preferences[PREF_METADATA_LANGUAGE]
        ?.let { code -> Language.fromCode(code) }
        ?: Language.ENGLISH,
      region = preferences[PREF_REGION_CODE]
        ?.let { code -> Country.fromCode(code) }
        ?: Country.UNITED_STATES,
    )
  }.distinctUntilChanged()

  override suspend fun selectTheme(theme: String) {
    dataStore.edit {
      it[PREF_SELECTED_THEME] = theme
    }
  }

  override val selectedTheme = dataStore.data.map {
    it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey
  }

  override suspend fun setBlackBackgrounds(isEnabled: Boolean) {
    dataStore.edit {
      it[PREF_BLACK_BACKGROUNDS] = isEnabled
    }
  }

  override val isBlackBackgroundsEnabled: Flow<Boolean> = dataStore.data.map {
    it[PREF_BLACK_BACKGROUNDS] ?: false
  }

  override suspend fun setSpoilersObfuscation(isEnabled: Boolean) {
    dataStore.edit {
      it[PREF_SERIES_TOTAL_EPISODES_OBFUSCATION] = isEnabled
    }
  }

  override val spoilersObfuscation = dataStore.data.mapLatest {
    it[PREF_SERIES_TOTAL_EPISODES_OBFUSCATION] ?: false
  }.distinctUntilChanged()

  override suspend fun setMovieRatingSource(ratingSource: RatingSource) {
    dataStore.edit { it[PREF_MOVIE_RATING_SOURCE] = ratingSource.value }
  }

  override val movieRatingSource: Flow<RatingSource> = dataStore.data.map { it ->
    it[PREF_MOVIE_RATING_SOURCE]?.let { RatingSource.from(it) } ?: RatingSource.TMDB
  }.distinctUntilChanged()

  override suspend fun setTvRatingSource(ratingSource: RatingSource) {
    dataStore.edit { it[PREF_TV_RATING_SOURCE] = ratingSource.value }
  }

  override val tvRatingSource: Flow<RatingSource> = dataStore.data.map { it ->
    it[PREF_TV_RATING_SOURCE]?.let { RatingSource.from(it) } ?: RatingSource.TMDB
  }.distinctUntilChanged()

  override suspend fun setEpisodesRatingSource(ratingSource: RatingSource) {
    dataStore.edit { it[PREF_EPISODES_RATING_SOURCE] = ratingSource.value }
  }

  override val episodesRatingSource: Flow<RatingSource> = dataStore.data.map { it ->
    it[PREF_EPISODES_RATING_SOURCE]?.let { RatingSource.from(it) } ?: RatingSource.TMDB
  }.distinctUntilChanged()

  override suspend fun setSeasonsRatingSource(ratingSource: RatingSource) {
    dataStore.edit { it[PREF_SEASONS_RATING_SOURCE] = ratingSource.value }
  }

  override val seasonsRatingSource: Flow<RatingSource> = dataStore.data.map { it ->
    it[PREF_SEASONS_RATING_SOURCE]?.let { RatingSource.from(it) } ?: RatingSource.TMDB
  }.distinctUntilChanged()

  override suspend fun setColorSystem(preference: ColorSystem) {
    dataStore.edit { it[PREF_COLOR_PREFERENCE] = preference.value }
  }

  override val colorSystem: Flow<ColorSystem> = dataStore.data.map { it ->
    it[PREF_COLOR_PREFERENCE]?.let { ColorSystem.from(it) } ?: ColorSystem.Dynamic
  }.distinctUntilChanged()

  override suspend fun setThemeColor(color: Long) {
    dataStore.edit { it[PREF_CUSTOM_COLOR_ID] = color }
  }

  override val customColor: Flow<Long> = dataStore.data.map {
    it[PREF_CUSTOM_COLOR_ID] ?: seedLong
  }.distinctUntilChanged()

  override suspend fun setRegion(country: Country) {
    dataStore.edit { it[PREF_REGION_CODE] = country.code }
  }

  override suspend fun setStreamingServicesVisible(visible: Boolean) {
    dataStore.edit { it[PREF_STREAMING_SERVICES_VISIBLE] = visible }
  }

  override suspend fun setUpdaterOptIn(checkForUpdates: Boolean) {
    dataStore.edit { it[PREF_UPDATER_OPT_IN] = checkForUpdates }
  }

  override suspend fun setMetadataLanguage(language: Language) {
    dataStore.edit { it[PREF_METADATA_LANGUAGE] = language.code }
  }

  override val appLanguage: Flow<AppLanguage> = dataStore.data.map {
    it[PREF_APP_LANGUAGE]?.let { locale -> AppLanguage.from(locale) } ?: AppLanguage.DEFAULT
  }.distinctUntilChanged()

  override suspend fun setAppLanguage(language: AppLanguage) {
    val locale = language.localeName
    if (locale == null) {
      dataStore.removeKey(PREF_APP_LANGUAGE)
    } else {
      dataStore.edit { it[PREF_APP_LANGUAGE] = locale }
    }
  }

  override val homeSections: Flow<HomeSectionsConfig>
    get() = dataStore.data.map { prefs ->
      val hidden = prefs[homeSectionHiddenKey]
        ?.mapNotNull { key -> MediaListSection.from(key) }
        ?.toSet()
        ?: emptySet()

      val storedOrder = prefs[homeSectionDisplayOrderKey]
        ?.split(",")
        ?.mapNotNull { key -> MediaListSection.from(key) }
        ?: emptyList()

      val order = MediaListSection.homeEntries
        .sortedBy { section ->
          val storedIndex = storedOrder.indexOf(section)
          if (storedIndex != -1) storedIndex else MediaListSection.homeEntries.indexOf(section)
        }

      HomeSectionsConfig(
        hidden = hidden,
        order = order,
      )
    }

  override suspend fun toggleHomeSectionVisibility(section: MediaListSection.Home) {
    dataStore.edit { preferences ->
      val currentHidden = preferences[homeSectionHiddenKey].orEmpty()

      if (section.key in currentHidden) {
        preferences[homeSectionHiddenKey] = currentHidden - section.key
      } else {
        preferences[homeSectionHiddenKey] = currentHidden + section.key
      }
    }
  }

  override suspend fun updateHomeSectionsOrder(sections: List<MediaListSection.Home>) {
    dataStore.edit { preferences ->
      preferences[homeSectionDisplayOrderKey] = sections.joinToString(separator = ",") { it.key }
    }
  }

  override suspend fun setBackupPath(path: String) {
    dataStore.edit { it[BACKUP_PATH] = path }
  }

  override suspend fun setAutomaticBackups(enabled: Boolean) {
    dataStore.edit { it[AUTOMATIC_BACKUPS] = enabled }
  }
}
