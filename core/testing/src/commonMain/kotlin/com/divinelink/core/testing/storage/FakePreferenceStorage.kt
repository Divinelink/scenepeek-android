package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.seedLong
import com.divinelink.core.model.details.rating.RatingSource
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
}
