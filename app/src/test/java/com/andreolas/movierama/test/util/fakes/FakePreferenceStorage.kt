package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.base.storage.PreferenceStorage
import kotlinx.coroutines.flow.MutableStateFlow

open class FakePreferenceStorage(
  selectedTheme: String = "",
  encryptedPreferences: String = "secret.preferences",
  isMaterialYouEnabled: Boolean = false,
  isBlackBackgroundsEnabled: Boolean = false,
  token: String? = null,
  hasSession: Boolean = false
) : PreferenceStorage {

  private val _selectedTheme = MutableStateFlow(selectedTheme)
  override val selectedTheme = _selectedTheme

  private val _encryptedPreferences = MutableStateFlow(encryptedPreferences)
  override val encryptedPreferences = _encryptedPreferences

  private val _isMaterialYouEnabled = MutableStateFlow(isMaterialYouEnabled)
  override val isMaterialYouEnabled = _isMaterialYouEnabled

  private val _isBlackBackgroundsEnabled = MutableStateFlow(isBlackBackgroundsEnabled)
  override val isBlackBackgroundsEnabled = _isBlackBackgroundsEnabled

  private val _token = MutableStateFlow(token)
  override val token = _token

  private val _hasSession = MutableStateFlow(hasSession)
  override val hasSession = _hasSession

  override suspend fun selectTheme(theme: String) {
    _selectedTheme.value = theme
  }

  override suspend fun setEncryptedPreferences(value: String) {
    _encryptedPreferences.value = value
  }

  override suspend fun setMaterialYou(isEnabled: Boolean) {
    _isMaterialYouEnabled.value = isEnabled
  }

  override suspend fun setBlackBackgrounds(isEnabled: Boolean) {
    _isBlackBackgroundsEnabled.value = isEnabled
  }

  override suspend fun clearToken() {
    _token.value = null
  }

  override suspend fun setToken(token: String) {
    _token.value = token
  }

  override suspend fun setHasSession(hasSession: Boolean) {
    _hasSession.value = hasSession
  }
}
