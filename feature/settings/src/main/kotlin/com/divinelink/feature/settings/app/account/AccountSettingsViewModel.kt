package com.divinelink.feature.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveSessionUseCase
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.feature.settings.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
  private val createRequestTokenUseCase: CreateRequestTokenUseCase,
  private val observeSessionUseCase: ObserveSessionUseCase,
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  getJellyseerrDetailsUseCase: GetJellyseerrAccountDetailsUseCase,
  private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

  private val _viewState = MutableStateFlow(AccountSettingsViewState.initial())
  val viewState: StateFlow<AccountSettingsViewState> = _viewState

  init {
    observeSession()

    getJellyseerrDetailsUseCase.invoke(false)
      .distinctUntilChanged()
      .onEach { result ->
        result
          .onSuccess { jellyseerrDetails ->
            _viewState.update {
              it.copy(jellyseerrAccountDetails = jellyseerrDetails)
            }
          }
          .onFailure {
            _viewState.update {
              it.copy(jellyseerrAccountDetails = null)
            }
          }
      }.launchIn(viewModelScope)
  }

  private fun observeSession() {
    observeSessionUseCase.invoke(Unit)
      .distinctUntilChanged()
      .onEach { result ->
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
        .onSuccess { requestToken ->
          val loginUrl = createLoginUrl(requestToken)
          _viewState.update {
            it.copy(loginUrl = loginUrl)
          }
        }
    }
  }

  fun logoutDialog() {
    _viewState.update {
      it.copy(
        alertDialogUiState = AlertDialogUiState(
          title = UIText.ResourceText(R.string.AccountSettingsScreen__logout),
          text = UIText.ResourceText(
            R.string.feature_settings_currently_login_dialog_summary,
            it.accountDetails?.username ?: "",
          ),
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
            alertDialogUiState = null,
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

  fun onConsumeLoginUrl() {
    _viewState.update {
      it.copy(loginUrl = null)
    }
  }

  private fun createLoginUrl(token: String): String {
    val redirectUrl = "scenepeek://auth/redirect"
    return "https://www.themoviedb.org/authenticate/$token?redirect_to=$redirectUrl"
  }
}
