package com.andreolas.movierama

import androidx.lifecycle.ViewModel
import com.andreolas.movierama.ui.ThemedActivityDelegate
import com.divinelink.core.commons.extensions.extractDetailsFromDeepLink
import com.divinelink.feature.details.ui.DetailsNavArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(themedActivityDelegate: ThemedActivityDelegate) :
  ViewModel(),
  ThemedActivityDelegate by themedActivityDelegate {

  private val _viewState: MutableStateFlow<MainUiState> =
    MutableStateFlow(MainUiState.Completed)
  val viewState: StateFlow<MainUiState> = _viewState

  private val _uiEvent: MutableStateFlow<MainUiEvent> = MutableStateFlow(MainUiEvent.None)
  val uiEvent: StateFlow<MainUiEvent> = _uiEvent

  private fun updateUiEvent(event: MainUiEvent) {
    _uiEvent.value = event
  }

  fun consumeUiEvent() {
    _uiEvent.value = MainUiEvent.None
  }

  fun handleDeepLink(url: String?) {
    val (id, mediaType) = url.extractDetailsFromDeepLink() ?: return

    updateUiEvent(
      MainUiEvent.NavigateToDetails(
        DetailsNavArguments(
          id = id,
          mediaType = mediaType,
          isFavorite = false,
        ),
      ),
    )
  }

  /**
   * Activate remote config once Main Activity starts.
   * This is crucial since we can fetch data from remote config and then update our UI
   * once we're ready.
   */
  /*
    init {
      setRemoteConfig()
    }

    fun retryFetchRemoteConfig() {
      setRemoteConfig()
    }

    private fun setRemoteConfig() {
      _viewState.value = MainViewState.Loading
      viewModelScope.launch {
        val result = setRemoteConfigUseCase.invoke(Unit)

        if (result.isSuccess) {
          _viewState.value = MainViewState.Completed
        } else {
          _viewState.value = MainViewState.Error(
            UIText.StringText("Something went wrong. Trying again..."),
          )
        }
      }
    }
   */
}
