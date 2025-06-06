package com.divinelink.feature.tmdb.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.session.TMDB_AUTH_DELAY
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TMDBAuthViewModel(private val createRequestTokenUseCase: CreateRequestTokenUseCase) :
  ViewModel() {

  private val _openUrlTab = Channel<String>()
  val openUrlTab: Flow<String> = _openUrlTab.receiveAsFlow()

  private val _onNavigateUp = Channel<Unit>()
  val onNavigateUp: Flow<Unit> = _onNavigateUp.receiveAsFlow()

  init {
    viewModelScope.launch {
      createRequestToken()
    }
  }

  private suspend fun createRequestToken() {
    createRequestTokenUseCase.invoke(Unit)
      .onSuccess { requestToken ->
        val loginUrl = createLoginUrl(requestToken)

        _openUrlTab.send(loginUrl)
      }
  }

  /**
   * There's a delay to make sure the session is properly created before proceeding
   * Also check [com.divinelink.core.domain.session.CreateSessionUseCase]
   */
  fun handleCloseWeb() {
    viewModelScope.launch {
      delay(TMDB_AUTH_DELAY)
      _onNavigateUp.trySend(Unit)
    }
  }

  private fun createLoginUrl(token: String): String {
    val redirectUrl = "scenepeek://auth/redirect"
    return "https://www.themoviedb.org/authenticate/$token?redirect_to=$redirectUrl"
  }
}
