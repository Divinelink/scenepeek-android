package com.divinelink.feature.settings.app.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.settings.MediaRatingPreferenceUseCase
import com.divinelink.core.model.details.rating.MediaRatingSource
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.locale.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsPreferencesViewModel(
  private val preferencesRepository: PreferencesRepository,
  private val mediaRatingPreferenceUseCase: MediaRatingPreferenceUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(DetailsPreferencesUiState.initial())
  val uiState: StateFlow<DetailsPreferencesUiState> = _uiState

  init {
    mediaRatingPreferenceUseCase(Unit)
      .onEach { result ->
        result.onSuccess { (source, rating) ->
          when (source) {
            is MediaRatingSource.Movie -> _uiState.value = _uiState.value.copy(movieSource = rating)
            is MediaRatingSource.TVShow -> _uiState.value = _uiState.value.copy(tvSource = rating)
            is MediaRatingSource.Episodes -> _uiState.value = _uiState.value.copy(
              episodesSource = rating,
            )
            is MediaRatingSource.Seasons -> _uiState.value = _uiState.value.copy(
              seasonsSource = rating,
            )
          }
        }
      }
      .launchIn(viewModelScope)

    preferencesRepository
      .detailPreferences
      .onEach { detailPreferences ->
        _uiState.update { uiState ->
          uiState.copy(detailPreferences = detailPreferences)
        }
      }
      .launchIn(viewModelScope)
  }

  fun setMediaRatingSource(source: Pair<MediaRatingSource, RatingSource>) {
    viewModelScope.launch {
      mediaRatingPreferenceUseCase.setMediaRatingSource(source)
    }
  }

  fun setRegion(country: Country) {
    viewModelScope.launch {
      preferencesRepository.setRegion(country)
    }
  }

  fun setStreamingServicesVisible(visible: Boolean) {
    viewModelScope.launch {
      preferencesRepository.setStreamingServicesVisible(visible)
    }
  }
}
