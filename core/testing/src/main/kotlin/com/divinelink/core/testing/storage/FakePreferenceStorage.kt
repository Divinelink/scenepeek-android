package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.details.rating.RatingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferenceStorage(
  selectedTheme: String = "",
  encryptedPreferences: String = "secret.preferences",
  isMaterialYouEnabled: Boolean = false,
  isBlackBackgroundsEnabled: Boolean = false,
  hasSession: Boolean = false,
  accountId: String? = null,
  jellyseerrAddress: String? = null,
  jellyseerrAccount: String? = null,
  jellyseerrSignInMethod: String? = null,
  spoilersObfuscation: Boolean = false,
  movieRatingSource: RatingSource = RatingSource.TMDB,
  tvRatingSource: RatingSource = RatingSource.TMDB,
  episodesRatingSource: RatingSource = RatingSource.TMDB,
  seasonsRatingSource: RatingSource = RatingSource.TMDB,
) : PreferenceStorage {

  private val _selectedTheme = MutableStateFlow(selectedTheme)
  override val selectedTheme = _selectedTheme

  private val _encryptedPreferences = MutableStateFlow(encryptedPreferences)
  override val encryptedPreferences = _encryptedPreferences

  private val _isMaterialYouEnabled = MutableStateFlow(isMaterialYouEnabled)
  override val isMaterialYouEnabled = _isMaterialYouEnabled

  private val _isBlackBackgroundsEnabled = MutableStateFlow(isBlackBackgroundsEnabled)
  override val isBlackBackgroundsEnabled = _isBlackBackgroundsEnabled

  private val _hasSession = MutableStateFlow(hasSession)
  override val hasSession = _hasSession

  private val _accountId = MutableStateFlow(accountId)
  override val accountId = _accountId

  private val _jellyseerrAddress = MutableStateFlow(jellyseerrAddress)
  override val jellyseerrAddress: Flow<String?> = _jellyseerrAddress

  private val _jellyseerrAccount = MutableStateFlow(jellyseerrAccount)
  override val jellyseerrAccount: Flow<String?> = _jellyseerrAccount

  private val _jellyseerrAuthMethod = MutableStateFlow(jellyseerrSignInMethod)
  override val jellyseerrAuthMethod: Flow<String?> = _jellyseerrAuthMethod

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
    _jellyseerrAuthMethod.value = null
  }

  override suspend fun setJellyseerrAuthMethod(authMethod: String) {
    _jellyseerrAuthMethod.value = authMethod
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
}
