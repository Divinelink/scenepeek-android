package com.divinelink.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.FetchWatchlistUseCase
import com.divinelink.core.domain.WatchlistParameters
import com.divinelink.core.domain.WatchlistResponse
import com.divinelink.core.model.media.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
  private val fetchWatchlistUseCase: FetchWatchlistUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<WatchlistUiState> = MutableStateFlow(
    WatchlistUiState(
      selectedTab = WatchlistTab.MOVIE.ordinal,
      tabs = WatchlistTab.entries,
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1
      ),
      forms = mapOf(
        MediaType.MOVIE to WatchlistForm.Loading,
        MediaType.TV to WatchlistForm.Loading
      )
    )
  )
  val uiState: StateFlow<WatchlistUiState> = _uiState

  init {
    viewModelScope.launch {
      fetchWatchlistUseCase.invoke(
        parameters = WatchlistParameters(
          page = _uiState.value.moviesPage,
          mediaType = MediaType.MOVIE,
        )
      ).collectLatest { result ->
        result.onSuccess { response ->
          updateUiState(response)
        }.onFailure {

        }
      }
    }
  }

  fun onTabSelected(tab: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedTab = tab)
    }

    if (_uiState.value.tvFormIsLoading) {
      viewModelScope.launch {
        fetchWatchlistUseCase.invoke(
          parameters = WatchlistParameters(
            page = uiState.value.tvPage,
            mediaType = MediaType.TV,
          )
        ).collectLatest { result ->
          result.onSuccess { response ->
            updateUiState(response)
          }.onFailure {

          }
        }
      }
    }
  }

  private fun updateUiState(
    response: WatchlistResponse
  ) {
    _uiState.update { uiState ->
      val currentData = uiState.forms[response.type] as? WatchlistForm.Data
      val currentPage = uiState.pages[response.type] ?: 1

      uiState.copy(
        forms = uiState.forms +
          (response.type to WatchlistForm.Data(currentData?.data.orEmpty() + response.data)),
        pages = uiState.pages + (response.type to currentPage + 1)
      ).run {
        Timber.d("Updating Ui for ${response.type} with data ${response.data}")
        Timber.d("Update page for ${response.type} to ${currentPage + 1}")
        this
      }
    }
  }
}
