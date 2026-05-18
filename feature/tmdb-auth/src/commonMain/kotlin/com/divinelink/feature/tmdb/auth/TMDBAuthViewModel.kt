package com.divinelink.feature.tmdb.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.session.AwaitSessionUseCase
import com.divinelink.core.domain.session.CreateSessionUseCase
import com.divinelink.core.network.session.util.buildRequestTokenApproveUrl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TMDBAuthViewModel(
  private val createRequestTokenUseCase: CreateRequestTokenUseCase,
  private val createSessionUseCase: CreateSessionUseCase,
  private val awaitSessionUseCase: AwaitSessionUseCase,
) : ViewModel() {

  private val _openUrlTab = Channel<String>()
  val openUrlTab: Flow<String> = _openUrlTab.receiveAsFlow()

  private val _onNavigateUp = Channel<Unit>()
  val onNavigateUp: Flow<Unit> = _onNavigateUp.receiveAsFlow()

  private val _uiState: MutableStateFlow<TMDBAuthUiState> = MutableStateFlow(
    TMDBAuthUiState.initial,
  )
  val uiState: StateFlow<TMDBAuthUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      createRequestToken()
    }
  }

  private suspend fun createRequestToken() {
    createRequestTokenUseCase.invoke(Unit)
      .onSuccess { requestToken ->
        val loginUrl = buildRequestTokenApproveUrl(requestToken)

        _uiState.update { uiState ->
          uiState.copy(url = loginUrl)
        }
        _openUrlTab.send(loginUrl)
      }
  }

  /**
   * On the Custom Tab path the session is created by MainViewModel after the auth
   * deeplink is caught. Wait for it to actually land in storage before navigating
   * up so the caller screen doesn't see a stale (empty) session on slow networks.
   */
  fun handleCloseWeb(createSession: Boolean) {
    if (createSession) {
      createSession()
    } else {
      viewModelScope.launch {
        awaitSessionUseCase.invoke(Unit)
        _onNavigateUp.trySend(Unit)
      }
    }
  }

  fun setWebViewFallback() {
    _uiState.update { uiState -> uiState.copy(webViewFallback = true) }
  }

  private fun createSession() {
    viewModelScope.launch {
      createSessionUseCase.invoke(Unit)
        .onSuccess {
          _onNavigateUp.send(Unit)
        }
        .onFailure {
          _onNavigateUp.send(Unit)
        }
    }
  }
}
