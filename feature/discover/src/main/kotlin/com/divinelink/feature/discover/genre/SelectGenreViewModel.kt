package com.divinelink.feature.discover.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.GenreRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectGenreViewModel(
  mediaType: MediaType,
  private val repository: MediaRepository,
  private val genreRepository: GenreRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SelectGenreUiState> = MutableStateFlow(
    SelectGenreUiState.initial,
  )
  val uiState: StateFlow<SelectGenreUiState> = _uiState

  init {
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

  fun onAction(action: SelectGenreAction) {
    when (action) {
      is SelectGenreAction.SelectGenre -> handleSelectGenre(action)
    }
  }

  private fun handleSelectGenre(action: SelectGenreAction.SelectGenre) {
    _uiState.update { uiState ->
      val updatedGenres = if (action.genre in uiState.selectedGenres) {
        uiState.selectedGenres - action.genre
      } else {
        uiState.selectedGenres + action.genre
      }

      genreRepository.updateSelectedGenres(updatedGenres.toSet())
      uiState.copy(selectedGenres = updatedGenres)
    }
  }
}
