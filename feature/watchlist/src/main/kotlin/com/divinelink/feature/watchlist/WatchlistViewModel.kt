package com.divinelink.feature.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.domain.FetchWatchlistUseCase
import com.divinelink.core.domain.session.ObserveSessionUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.watchlist.WatchlistParameters
import com.divinelink.core.model.watchlist.WatchlistResponse
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
  private val observeSessionUseCase: ObserveSessionUseCase,
  private val fetchWatchlistUseCase: FetchWatchlistUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<WatchlistUiState> = MutableStateFlow(
    WatchlistUiState(
      selectedTabIndex = WatchlistTab.MOVIE.ordinal,
      tabs = WatchlistTab.entries,
      pages = mapOf(
        MediaType.MOVIE to 1,
        MediaType.TV to 1,
      ),
      forms = mapOf(
        MediaType.MOVIE to WatchlistForm.Loading,
        MediaType.TV to WatchlistForm.Loading,
      ),
      canFetchMore = mapOf(
        MediaType.MOVIE to true,
        MediaType.TV to true,
      ),
    ),
  )
  val uiState: StateFlow<WatchlistUiState> = _uiState

  init {
    viewModelScope.launch {
      observeSessionUseCase.invoke(Unit).collectLatest { result ->
        result.onSuccess {
          fetchWatchlist(MediaType.TV)
          fetchWatchlist(MediaType.MOVIE)
        }.onFailure { throwable ->
          updateUiOnFailure(MediaType.TV, throwable)
          updateUiOnFailure(MediaType.MOVIE, throwable)
          resetPages()
        }
      }
    }
  }

  fun onLoadMore() {
    val uiState = _uiState.value
    val mediaType = uiState.mediaType
    val canFetchMore = uiState.canFetchMore[mediaType] ?: false

    if (canFetchMore) {
      fetchWatchlist(mediaType = mediaType)
    }
  }

  fun onTabSelected(tab: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedTabIndex = tab)
    }
  }

  private fun fetchWatchlist(mediaType: MediaType) {
    viewModelScope.launch {
      fetchWatchlistUseCase.invoke(
        WatchlistParameters(
          page = uiState.value.pages[mediaType] ?: 1,
          mediaType = mediaType,
        ),
      ).collectLatest { result ->
        result
          .onSuccess { response ->
            updateUiOnSuccess(response)
          }
          .onFailure { throwable ->
            updateUiOnFailure(
              mediaType = mediaType,
              throwable = throwable,
            )
          }
      }
    }
  }

  /**
   * Reset pages to 1 when the user logs out or when swipe to refresh is triggered.
   */
  private fun resetPages() {
    _uiState.update { uiState ->
      uiState.copy(
        pages = uiState.pages.mapValues { (_, _) -> 1 },
      )
    }
  }

  private fun updateUiOnFailure(
    mediaType: MediaType,
    throwable: Throwable,
  ) {
    if (throwable is SessionException.Unauthenticated) {
      _uiState.update {
        it.copy(
          forms = it.forms.entries.associate { (mediaType, _) ->
            mediaType to WatchlistForm.Error.Unauthenticated
          },
        )
      }
    } else {
      _uiState.update {
        it.copy(
          forms = it.forms + (mediaType to WatchlistForm.Error.Unknown),
        )
      }
    }
  }

  private fun updateUiOnSuccess(response: WatchlistResponse) {
    _uiState.update { uiState ->
      val currentData = (uiState.forms[response.type] as? WatchlistForm.Data)?.data.orEmpty()
      val currentPage = uiState.pages[response.type] ?: 1

      uiState.copy(
        forms = uiState.forms + (
          response.type to WatchlistForm.Data(
            mediaType = response.type,
            data = currentData + response.data,
            totalResults = response.totalResults,
          )
          ),
        pages = uiState.pages + (response.type to currentPage + 1),
        canFetchMore = uiState.canFetchMore + (response.type to response.canFetchMore),
      ).run {
        Timber.d("Updating Ui for ${response.type} with data ${response.data}")
        Timber.d("Update page for ${response.type} to ${currentPage + 1}")
        Timber.d("Can fetch more for ${response.type} is ${response.canFetchMore}")
        this
      }
    }
  }
}
