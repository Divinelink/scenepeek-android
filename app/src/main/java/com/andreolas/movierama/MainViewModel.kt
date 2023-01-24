package com.andreolas.movierama

import androidx.lifecycle.ViewModel
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.theme.ThemedActivityDelegate
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    themedActivityDelegate: ThemedActivityDelegate,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : ViewModel(),
    ThemedActivityDelegate by themedActivityDelegate {

    private val _viewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState.Loading)
    val viewState: StateFlow<MainViewState> = _viewState

    /**
     * Activate remote config once Main Activity starts.
     * This is crucial since we can fetch data from remote config and then update our UI
     * once we're ready.
     */
    init {
        initialiseRemoteConfig()
    }

    fun onFetchRemoteAgain() {
        initialiseRemoteConfig()
    }

    private fun initialiseRemoteConfig() {
        _viewState.value = MainViewState.Loading
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { remoteTask ->
                if (remoteTask.isSuccessful) {
                    _viewState.value = MainViewState.Completed
                } else {
                    _viewState.value = MainViewState.Error(
                        UIText.StringText("Something went wrong. Trying again...")
                    )
                }
            }
    }
}
