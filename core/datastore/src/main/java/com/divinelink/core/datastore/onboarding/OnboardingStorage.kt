package com.divinelink.core.datastore.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.divinelink.core.commons.BuildConfig
import kotlinx.coroutines.flow.Flow

interface OnboardingStorage {
  suspend fun setOnboardingCompleted()
  val onboardingCompleted: Flow<Boolean>
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

  override suspend fun setOnboardingCompleted() {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.IS_FIRST_LAUNCH] = false
      preferences[PreferencesKeys.LAST_SEEN_VERSION] = BuildConfig.VERSION_CODE
    }
  }

  override val onboardingCompleted: Flow<Boolean>
    get() = TODO("Not yet implemented")
}
