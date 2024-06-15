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
      moviesPage = 1,
      tvPage = 1,
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
          page = uiState.value.moviesPage,
        )
      ).collectLatest { result ->
        result.onSuccess { response ->
          updateUiState(response)
        }.onFailure {

        }
      }
    }
  }

  private fun updateUiState(
    response: WatchlistResponse
  ) {
    _uiState.update { uiState ->
      val currentData = uiState.forms[response.type] as? WatchlistForm.Data

      uiState.copy(
        moviesPage = uiState.moviesPage + 1,
        forms = uiState.forms +
          (response.type to WatchlistForm.Data(currentData?.data.orEmpty() + response.data))
      ).run {
        Timber.d("Updating Ui for ${response.type} with data ${response.data}")
        this
      }
    }
  }
}
