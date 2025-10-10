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
import com.divinelink.feature.discover.FilterType
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
        filterRepository.selectedCountry.onEach { country ->
          _uiState.update {
            it.copy(
              filterType = (it.filterType as FilterType.Countries).copy(
                selectedOptions = country?.let { listOf(country) } ?: emptyList(),
              ),
            )
          }
        }.launchIn(viewModelScope)
      }
      FilterModal.Genre -> {
        fetchGenres(mediaType)
        filterRepository.selectedGenres.onEach { genres ->
          _uiState.update {
            it.copy(
              filterType = (it.filterType as FilterType.Genres).copy(
                selectedOptions = genres,
              ),
            )
          }
        }.launchIn(viewModelScope)
      }
      FilterModal.Language -> {
        filterRepository.selectedLanguage.onEach { language ->
          _uiState.update {
            it.copy(
              filterType = (it.filterType as FilterType.Languages).copy(
                selectedOptions = language?.let { listOf(language) } ?: emptyList(),
              ),
            )
          }
        }.launchIn(viewModelScope)
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

      repository.fetchGenres(mediaType).distinctUntilChanged().onEach { result ->
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
              filterType = FilterType.Genres(
                options = result.data ?: emptyList(),
                selectedOptions = emptyList(),
                query = null,
              ),
              error = null,
            )
          }
          is Resource.Success -> _uiState.update { uiState ->
            uiState.copy(
              loading = false,
              filterType = FilterType.Genres(
                options = result.data,
                selectedOptions = emptyList(),
                query = null,
              ),
              error = null,
            )
          }
        }
      }.launchIn(viewModelScope)
    }
  }

  fun onAction(action: SelectFilterAction) {
    when (action) {
      SelectFilterAction.ClearGenres -> handleClearGenres()
      SelectFilterAction.Retry -> handleRetry()
      is SelectFilterAction.SelectGenre -> handleSelectGenre(action)
      is SelectFilterAction.SelectLanguage -> handleSelectLanguage(action)
      is SelectFilterAction.SelectCountry -> handleSelectCountry(action)
      is SelectFilterAction.SearchFilters -> handleSearchFilters(action)
    }
  }

  private fun handleSearchFilters(action: SelectFilterAction.SearchFilters) {
    _uiState.update { uiState ->
      uiState.copy(
        filterType = when (uiState.filterType) {
          is FilterType.Countries -> uiState.filterType.copy(query = action.query)
          is FilterType.Genres -> uiState.filterType.copy(query = action.query)
          is FilterType.Languages -> uiState.filterType.copy(query = action.query)
        },
      )
    }
  }

  private fun handleClearGenres() {
    filterRepository.updateSelectedGenres(emptyList())
  }

  private fun handleRetry() {
    when (uiState.value.filterModal) {
      FilterModal.Genre -> fetchGenres(uiState.value.mediaType)
      FilterModal.Country -> Unit
      FilterModal.Language -> Unit
    }
  }

  private fun handleSelectGenre(action: SelectFilterAction.SelectGenre) {
    val selectedGenres = (_uiState.value.filterType as FilterType.Genres).selectedOptions

    val genres = if (action.genre in selectedGenres) {
      selectedGenres - action.genre
    } else {
      selectedGenres + action.genre
    }

    filterRepository.updateSelectedGenres(genres)
  }

  private fun handleSelectLanguage(action: SelectFilterAction.SelectLanguage) {
    val selectedLanguage = (_uiState.value.filterType as FilterType.Languages).selectedOptions

    val language = if (action.language in selectedLanguage) {
      null
    } else {
      action.language
    }
    filterRepository.updateLanguage(language)
  }

  private fun handleSelectCountry(action: SelectFilterAction.SelectCountry) {
    val selectedCountry = (_uiState.value.filterType as FilterType.Countries).selectedOptions

    val language = if (action.country in selectedCountry) {
      null
    } else {
      action.country
    }
    filterRepository.updateCountry(language)
  }
}
