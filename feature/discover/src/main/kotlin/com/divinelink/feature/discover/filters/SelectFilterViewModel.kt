package com.divinelink.feature.discover.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.FilterRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.discover.FilterModal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectFilterViewModel(
  type: FilterModal,
  mediaType: MediaType,
  private val repository: MediaRepository,
  private val filterRepository: FilterRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SelectFilterUiState> = MutableStateFlow(
    SelectFilterUiState.initial(
      filterModal = type,
      mediaType = mediaType,
    ),
  )
  val uiState: StateFlow<SelectFilterUiState> = _uiState

  init {
    when (type) {
      FilterModal.Country -> {
        filterRepository
          .selectedCountry
          .onEach { country ->
            _uiState.update { it.copy(selectedCountry = country) }
          }
          .launchIn(viewModelScope)
      }
      FilterModal.Genre -> {
        fetchGenres(mediaType)
        filterRepository
          .selectedGenres
          .onEach { genres ->
            _uiState.update { it.copy(selectedGenres = genres) }
          }
          .launchIn(viewModelScope)
      }
      FilterModal.Language -> {
        filterRepository
          .selectedLanguage
          .onEach { language ->
            _uiState.update { it.copy(selectedLanguage = language) }
          }
          .launchIn(viewModelScope)
      }
    }
  }

  private fun fetchGenres(mediaType: MediaType) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          loading = true,
          error = null,
        )
      }

      repository
        .fetchGenres(mediaType)
        .distinctUntilChanged()
        .onEach { result ->
          when (result) {
            is Resource.Error -> _uiState.update {
              val blankSlate = when (result.error) {
                is AppException.Offline -> BlankSlateState.Offline
                else -> BlankSlateState.Generic
              }

              it.copy(
                error = blankSlate,
                loading = false,
              )
            }
            is Resource.Loading -> _uiState.update { uiState ->
              uiState.copy(
                loading = false,
                genres = result.data ?: emptyList(),
                error = null,
              )
            }
            is Resource.Success -> _uiState.update { uiState ->
              uiState.copy(
                loading = false,
                genres = result.data,
                error = null,
              )
            }
          }
        }
        .launchIn(viewModelScope)
    }
  }

  fun onAction(action: SelectFilterAction) {
    when (action) {
      SelectFilterAction.ClearGenres -> handleClearGenres()
      SelectFilterAction.Retry -> handleRetry()
      is SelectFilterAction.SelectGenre -> handleSelectGenre(action)
      is SelectFilterAction.SelectLanguage -> handleSelectLanguage(action)
      is SelectFilterAction.SelectCountry -> handleSelectCountry(action)
    }
  }

  private fun handleClearGenres() {
    _uiState.update { uiState ->
      filterRepository.updateSelectedGenres(emptyList())
      uiState.copy(selectedGenres = emptyList())
    }
  }

  private fun handleRetry() {
    when (uiState.value.filterModal) {
      FilterModal.Genre -> fetchGenres(uiState.value.mediaType)
      FilterModal.Country -> Unit
      FilterModal.Language -> Unit
    }
  }

  private fun handleSelectGenre(action: SelectFilterAction.SelectGenre) {
    val selectedGenres = _uiState.value.selectedGenres

    val genres = if (action.genre in selectedGenres) {
      selectedGenres - action.genre
    } else {
      selectedGenres + action.genre
    }

    filterRepository.updateSelectedGenres(genres)
  }

  private fun handleSelectLanguage(action: SelectFilterAction.SelectLanguage) {
    val language = if (action.language == uiState.value.selectedLanguage) {
      null
    } else {
      action.language
    }
    filterRepository.updateLanguage(language)
  }

  private fun handleSelectCountry(action: SelectFilterAction.SelectCountry) {
    val language = if (action.country == uiState.value.selectedCountry) {
      null
    } else {
      action.country
    }
    filterRepository.updateCountry(language)
  }
}
