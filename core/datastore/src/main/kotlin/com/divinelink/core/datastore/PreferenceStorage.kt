package com.divinelink.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_BLACK_BACKGROUNDS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_EPISODES_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_JELLYSEERR_ACCOUNT
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_JELLYSEERR_ADDRESS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_JELLYSEERR_AUTH_METHOD
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_MATERIAL_YOU
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_MOVIE_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SEASONS_RATING_SOURCE
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SERIES_TOTAL_EPISODES_OBFUSCATION
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_TV_RATING_SOURCE
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.model.details.rating.RatingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

interface PreferenceStorage {
  suspend fun selectTheme(theme: String)
  val selectedTheme: Flow<String>

  suspend fun setMaterialYou(isEnabled: Boolean)
  val isMaterialYouEnabled: Flow<Boolean>

  suspend fun setBlackBackgrounds(isEnabled: Boolean)
  val isBlackBackgroundsEnabled: Flow<Boolean>

  suspend fun setSpoilersObfuscation(isEnabled: Boolean)
  val spoilersObfuscation: Flow<Boolean>

  suspend fun clearJellyseerrAddress()
  suspend fun setJellyseerrAddress(address: String)
  val jellyseerrAddress: Flow<String?>

  suspend fun clearJellyseerrAccount()
  suspend fun setJellyseerrAccount(account: String)
  val jellyseerrAccount: Flow<String?>

  suspend fun clearJellyseerrSignInMethod()
  suspend fun setJellyseerrAuthMethod(authMethod: String)
  val jellyseerrAuthMethod: Flow<String?>

  suspend fun setMovieRatingSource(ratingSource: RatingSource)
  val movieRatingSource: Flow<RatingSource>

  suspend fun setTvRatingSource(ratingSource: RatingSource)
  val tvRatingSource: Flow<RatingSource>

  suspend fun setEpisodesRatingSource(ratingSource: RatingSource)
  val episodesRatingSource: Flow<RatingSource>

  suspend fun setSeasonsRatingSource(ratingSource: RatingSource)
  val seasonsRatingSource: Flow<RatingSource>
}

class DataStorePreferenceStorage(private val dataStore: DataStore<Preferences>) :
  PreferenceStorage {

  companion object {
    const val PREFS_NAME = "application_preferences"
  }

  object PreferencesKeys {
    val PREF_SELECTED_THEME = stringPreferencesKey("settings.theme")
    val PREF_MATERIAL_YOU = booleanPreferencesKey("settings.material.you")
    val PREF_BLACK_BACKGROUNDS = booleanPreferencesKey("settings.black.backgrounds")

    val PREF_SERIES_TOTAL_EPISODES_OBFUSCATION = booleanPreferencesKey(
      "settings.series.episode.obfuscation",
    )

    val PREF_JELLYSEERR_ADDRESS = stringPreferencesKey("jellyseerr.address")
    val PREF_JELLYSEERR_ACCOUNT = stringPreferencesKey("jellyseerr.account")
    val PREF_JELLYSEERR_AUTH_METHOD = stringPreferencesKey("jellyseerr.sign.in.method")

    val PREF_MOVIE_RATING_SOURCE = stringPreferencesKey("movie.rating.source")
    val PREF_TV_RATING_SOURCE = stringPreferencesKey("tv.rating.source")
    val PREF_EPISODES_RATING_SOURCE = stringPreferencesKey("episodes.rating.source")
    val PREF_SEASONS_RATING_SOURCE = stringPreferencesKey("seasons.rating.source")
  }

  override suspend fun selectTheme(theme: String) {
    dataStore.edit {
      Timber.d("Set theme to $theme")
      it[PREF_SELECTED_THEME] = theme
    }
  }

  override val selectedTheme = dataStore.data.map {
    it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey
  }

  override suspend fun setMaterialYou(isEnabled: Boolean) {
    dataStore.edit {
      it[PREF_MATERIAL_YOU] = isEnabled
    }
  }

  override val isMaterialYouEnabled = dataStore.data.map {
    it[PREF_MATERIAL_YOU] ?: true
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

  override suspend fun clearJellyseerrAddress() {
    dataStore.edit {
      it.remove(PREF_JELLYSEERR_ADDRESS)
    }
  }

  override suspend fun setJellyseerrAddress(address: String) {
    dataStore.edit {
      it[PREF_JELLYSEERR_ADDRESS] = address
    }
  }

  override val jellyseerrAddress: Flow<String?> = dataStore.data.map {
    it[PREF_JELLYSEERR_ADDRESS]
  }

  override suspend fun clearJellyseerrAccount() {
    dataStore.edit {
      it.remove(PREF_JELLYSEERR_ACCOUNT)
    }
  }

  override suspend fun setJellyseerrAccount(account: String) {
    dataStore.edit {
      it[PREF_JELLYSEERR_ACCOUNT] = account
    }
  }

  override val jellyseerrAccount: Flow<String?> = dataStore.data.map {
    it[PREF_JELLYSEERR_ACCOUNT]
  }

  override suspend fun clearJellyseerrSignInMethod() {
    dataStore.edit {
      it.remove(PREF_JELLYSEERR_AUTH_METHOD)
    }
  }

  override suspend fun setJellyseerrAuthMethod(authMethod: String) {
    dataStore.edit {
      it[PREF_JELLYSEERR_AUTH_METHOD] = authMethod
    }
  }

  override val jellyseerrAuthMethod: Flow<String?> = dataStore.data.map {
    it[PREF_JELLYSEERR_AUTH_METHOD]
  }

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
}
