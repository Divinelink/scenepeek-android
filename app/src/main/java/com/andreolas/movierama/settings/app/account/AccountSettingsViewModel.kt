package com.andreolas.movierama.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.session.login.domain.session.ObserveSessionUseCase
import com.andreolas.movierama.session.login.domain.token.CreateRequestTokenUseCase
import com.andreolas.movierama.settings.app.account.usecase.GetAccountDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
  private val createRequestTokenUseCase: CreateRequestTokenUseCase,
  private val observeSessionUseCase: ObserveSessionUseCase,
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase
) : ViewModel() {

  init {
    observeSession()
  }

  private val _viewState = MutableStateFlow(AccountSettingsViewState(requestToken = null))
  val viewState: StateFlow<AccountSettingsViewState> = _viewState

  private fun observeSession() {
    observeSessionUseCase.invoke(Unit).onEach { result ->
      result
        .onSuccess {
          println("User has session")
          fetchAccountDetails()
        }.onFailure {
          println("User does not have session")
        }
    }.launchIn(viewModelScope)
  }

  private fun fetchAccountDetails() {
    getAccountDetailsUseCase.invoke(Unit).onEach { result ->
      result
        .onSuccess { response ->
          println(response)
        }.onFailure {
          println("Error fetching account details")
        }
    }.launchIn(viewModelScope)
  }

  fun login() {
    viewModelScope.launch {
      createRequestTokenUseCase.invoke(Unit)
        .onSuccess { response ->
          _viewState.update {
            it.copy(
              requestToken = response.data,
              navigateToWebView = true
            )
          }
        }.onFailure {
          TODO()
        }
    }
  }

  fun onWebViewScreenNavigated() {
    _viewState.update {
      it.copy(navigateToWebView = null)
    }
  }
}
