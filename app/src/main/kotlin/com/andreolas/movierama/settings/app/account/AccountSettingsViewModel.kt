package com.andreolas.movierama.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreolas.movierama.session.login.domain.session.usecase.LogoutUseCase
import com.andreolas.movierama.session.login.domain.session.usecase.ObserveSessionUseCase
import com.andreolas.movierama.session.login.domain.token.usecase.CreateRequestTokenUseCase
import com.andreolas.movierama.settings.app.account.usecase.GetAccountDetailsUseCase
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.dialog.AlertDialogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  private val logoutUseCase: LogoutUseCase
) : ViewModel() {

  private val _viewState = MutableStateFlow(AccountSettingsViewState.initial())
  val viewState: StateFlow<AccountSettingsViewState> = _viewState

  init {
    observeSession()
  }

  private fun observeSession() {
    observeSessionUseCase.invoke(Unit).onEach { result ->
      result
        .onSuccess {
          println("User has session, fetching account details")
          fetchAccountDetails()
        }.onFailure {
          println("User does not have session")
        }
    }.launchIn(viewModelScope)
  }

  private fun fetchAccountDetails() {
    getAccountDetailsUseCase.invoke(Unit).onEach { result ->
      result
        .onSuccess { accountDetails ->
          _viewState.update {
            it.copy(accountDetails = accountDetails)
          }
        }
    }.launchIn(viewModelScope)
  }

  fun login() {
    viewModelScope.launch {
      createRequestTokenUseCase.invoke(Unit)
        .onSuccess { response ->
          _viewState.update {
            it.copy(
              requestToken = response,
              navigateToWebView = true
            )
          }
        }
    }
  }

  fun logoutDialog() {
    _viewState.update {
      it.copy(
        alertDialogUiState = AlertDialogUiState(
          title = UIText.StringText("Logout"),
          text = UIText.StringText(
            "You're currently logged in as" +
              " ${it.accountDetails?.username}. Are you sure you want to logout?"
          )
        ),
      )
    }
  }

  fun confirmLogout() {
    viewModelScope.launch {
      logoutUseCase.invoke(Unit).onSuccess {
        _viewState.update {
          it.copy(
            accountDetails = null,
            alertDialogUiState = null
          )
        }
      }
    }
  }

  fun dismissLogoutDialog() {
    _viewState.update {
      it.copy(alertDialogUiState = null)
    }
  }

  fun onWebViewScreenNavigated() {
    _viewState.update {
      it.copy(navigateToWebView = null)
    }
  }
}
