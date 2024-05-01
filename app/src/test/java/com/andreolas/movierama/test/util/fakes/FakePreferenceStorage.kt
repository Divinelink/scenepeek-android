package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.base.storage.PreferenceStorage
import kotlinx.coroutines.flow.MutableStateFlow

open class FakePreferenceStorage(
  selectedTheme: String = "",
  encryptedPreferences: String = "secret.preferences"
) : PreferenceStorage {

  private val _selectedTheme = MutableStateFlow(selectedTheme)
  override val selectedTheme = _selectedTheme

  private val _encryptedPreferences = MutableStateFlow(encryptedPreferences)
  override val encryptedPreferences = _encryptedPreferences

  override suspend fun selectTheme(theme: String) {
    _selectedTheme.value = theme
  }

  override suspend fun setEncryptedPreferences(value: String) {
    _encryptedPreferences.value = value
  }
}
