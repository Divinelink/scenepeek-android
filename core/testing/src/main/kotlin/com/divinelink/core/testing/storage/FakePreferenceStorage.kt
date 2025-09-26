package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.locale.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferenceStorage(
  selectedTheme: String = "",
  isMaterialYouEnabled: Boolean = false,
  isBlackBackgroundsEnabled: Boolean = false,
  spoilersObfuscation: Boolean = false,
  movieRatingSource: RatingSource = RatingSource.TMDB,
  tvRatingSource: RatingSource = RatingSource.TMDB,
  episodesRatingSource: RatingSource = RatingSource.TMDB,
  seasonsRatingSource: RatingSource = RatingSource.TMDB,
  metadataLanguage: Language = Language.ENGLISH,
) : PreferenceStorage {

  private val _selectedTheme = MutableStateFlow(selectedTheme)
  override val selectedTheme = _selectedTheme

  private val _isMaterialYouEnabled = MutableStateFlow(isMaterialYouEnabled)
  override val isMaterialYouEnabled = _isMaterialYouEnabled

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

  private val _metadataLanguage = MutableStateFlow(metadataLanguage)
  override val metadataLanguage: Flow<Language> = _metadataLanguage

  override suspend fun selectTheme(theme: String) {
    _selectedTheme.value = theme
  }

  override suspend fun setMaterialYou(isEnabled: Boolean) {
    _isMaterialYouEnabled.value = isEnabled
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

  override suspend fun setMetadataLanguage(language: Language) {
    _metadataLanguage.value = language
  }
}
