package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.GenreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DiscoverViewModel(genreRepository: GenreRepository) : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> = MutableStateFlow(
    DiscoverUiState.initial,
  )
  val uiState: StateFlow<DiscoverUiState> = _uiState

  init {
    genreRepository
      .selectedGenres
      .onEach { genres ->
        _uiState.update { uiState ->
          uiState.copy(
            genreFilters = uiState.genreFilters.plus(
              uiState.selectedTab.mediaType to genres.toList(),
            ),
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: DiscoverAction) {
    when (action) {
      is DiscoverAction.OnSelectTab -> handleSelectTab(action)
    }
  }

  private fun handleSelectTab(action: DiscoverAction.OnSelectTab) {
    _uiState.update {
      it.copy(selectedTabIndex = action.index)
    }
  }
}
