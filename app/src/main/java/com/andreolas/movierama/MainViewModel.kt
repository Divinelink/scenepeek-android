package com.andreolas.movierama

import androidx.lifecycle.ViewModel
import com.andreolas.movierama.ui.ThemedActivityDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  themedActivityDelegate: ThemedActivityDelegate,
) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate {

  private val _viewState: MutableStateFlow<MainViewState> =
    MutableStateFlow(MainViewState.Completed)
  val viewState: StateFlow<MainViewState> = _viewState

  /**
   * Activate remote config once Main Activity starts.
   * This is crucial since we can fetch data from remote config and then update our UI
   * once we're ready.
   */

  /**
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
          UIText.StringText("Something went wrong. Trying again...")
        )
      }
    }
  }
  */
}
