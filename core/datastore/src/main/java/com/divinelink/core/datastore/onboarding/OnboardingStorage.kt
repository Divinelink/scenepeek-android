package com.divinelink.core.datastore.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.divinelink.core.commons.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface OnboardingStorage {
  val isFirstLaunch: Flow<Boolean>
  val lastSeenVersion: Flow<Int>
  suspend fun setOnboardingCompleted()
  suspend fun updateLastSeenVersion(version: Int)
}

class DataStoreOnboardingStorage(private val dataStore: DataStore<Preferences>) :
  OnboardingStorage {

  companion object {
    const val PREFS_NAME = "onboarding_preferences"
  }

  private object PreferencesKeys {
    val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    val LAST_SEEN_VERSION = intPreferencesKey("last_seen_version")
  }

  override val isFirstLaunch: Flow<Boolean> = dataStore.data
    .map { preferences -> preferences[PreferencesKeys.IS_FIRST_LAUNCH] ?: true }

  override val lastSeenVersion: Flow<Int> = dataStore.data
    .map { preferences -> preferences[PreferencesKeys.LAST_SEEN_VERSION] ?: 0 }

  override suspend fun setOnboardingCompleted() {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.IS_FIRST_LAUNCH] = false
      preferences[PreferencesKeys.LAST_SEEN_VERSION] = BuildConfig.VERSION_CODE
    }
  }

  override suspend fun updateLastSeenVersion(version: Int) {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.LAST_SEEN_VERSION] = version
    }
  }
}
