package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

open class FakePreferenceStorage(
  selectedTheme: String = "",
  encryptedPreferences: String = "secret.preferences",
  isMaterialYouEnabled: Boolean = false,
  isBlackBackgroundsEnabled: Boolean = false,
  token: String? = null,
  hasSession: Boolean = false,
  accountId: String? = null,
  jellyseerrAddress: String? = null,
  jellyseerrAccount: String? = null,
  jellyseerrSignInMethod: String? = null,
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

  private val _accountId = MutableStateFlow(accountId)
  override val accountId = _accountId

  private val _jellyseerrAddress = MutableStateFlow(jellyseerrAddress)
  override val jellyseerrAddress: Flow<String?> = _jellyseerrAddress

  private val _jellyseerrAccount = MutableStateFlow(jellyseerrAccount)
  override val jellyseerrAccount: Flow<String?> = _jellyseerrAccount

  private val _jellyseerrSignInMethod = MutableStateFlow(jellyseerrSignInMethod)
  override val jellyseerrSignInMethod: Flow<String?> = _jellyseerrSignInMethod

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

  override suspend fun clearAccountId() {
    _accountId.value = null
  }

  override suspend fun setAccountId(accountId: String) {
    _accountId.value = accountId
  }

  override suspend fun clearJellyseerrAddress() {
    _jellyseerrAddress.value = null
  }

  override suspend fun setJellyseerrAddress(address: String) {
    _jellyseerrAddress.value = address
  }

  override suspend fun clearJellyseerrAccount() {
    _jellyseerrAccount.value = null
  }

  override suspend fun setJellyseerrAccount(account: String) {
    _jellyseerrAccount.value = account
  }

  override suspend fun clearJellyseerrSignInMethod() {
    _jellyseerrSignInMethod.value = null
  }

  override suspend fun setJellyseerrSignInMethod(signInMethod: String) {
    _jellyseerrSignInMethod.value = signInMethod
  }
}
