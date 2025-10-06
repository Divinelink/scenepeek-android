package com.divinelink.feature.discover.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.FilterRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaType
import com.divinelink.feature.discover.FilterModal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectFilterViewModel(
  type: FilterModal,
  mediaType: MediaType,
  private val repository: MediaRepository,
  private val filterRepository: FilterRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SelectFilterUiState> = MutableStateFlow(
    SelectFilterUiState.initial,
  )
  val uiState: StateFlow<SelectFilterUiState> = _uiState

  init {
    if (type is FilterModal.Genre) {
      viewModelScope.launch {
        if (mediaType == MediaType.TV) {
          repository
            .fetchTvGenres()
            .onSuccess { genres ->
              _uiState.update { uiState ->
                uiState.copy(
                  loading = false,
                  genres = genres,
                )
              }
            }
        } else {
          repository
            .fetchMovieGenres()
            .onSuccess { genres ->
              _uiState.update { uiState ->
                uiState.copy(
                  loading = false,
                  genres = genres,
                )
              }
            }
        }
      }
    }
  }

  fun onAction(action: SelectFilterAction) {
    when (action) {
      is SelectFilterAction.SelectGenre -> handleSelectGenre(action)
      is SelectFilterAction.SelectLanguage -> handleSelectLanguage(action)
    }
  }

  private fun handleSelectGenre(action: SelectFilterAction.SelectGenre) {
    _uiState.update { uiState ->
      val updatedGenres = if (action.genre in uiState.selectedGenres) {
        uiState.selectedGenres - action.genre
      } else {
        uiState.selectedGenres + action.genre
      }

      filterRepository.updateSelectedGenres(updatedGenres)
      uiState.copy(selectedGenres = updatedGenres)
    }
  }

  private fun handleSelectLanguage(action: SelectFilterAction.SelectLanguage) {
    _uiState.update { uiState ->
      val newLanguage = if (action.language == uiState.selectedLanguage) {
        null
      } else {
        action.language
      }

      filterRepository.updateLanguage(newLanguage)
      uiState.copy(selectedLanguage = newLanguage)
    }
  }
}
