package com.divinelink.feature.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.model.UIText
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.feature.settings.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  getJellyseerrDetailsUseCase: GetJellyseerrAccountDetailsUseCase,
  private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

  private val _viewState = MutableStateFlow(AccountSettingsViewState.initial())
  val viewState: StateFlow<AccountSettingsViewState> = _viewState

  private val _navigateToTMDBAuth = Channel<Unit>()
  val navigateToTMDBAuth: Flow<Unit> = _navigateToTMDBAuth.receiveAsFlow()

  init {
    fetchAccountDetails()

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

  private fun fetchAccountDetails() {
    viewModelScope.launch {
      getAccountDetailsUseCase.invoke(Unit).collect { result ->
        result
          .onSuccess { accountDetails ->
            _viewState.update {
              it.copy(tmdbAccount = accountDetails)
            }
          }
          .onFailure {
            ErrorHandler.create(it) {
              on(401) {
                _viewState.update { uiState ->
                  uiState.copy(tmdbAccount = TMDBAccount.Anonymous)
                }
              }
            }
          }
      }
    }
  }

  fun login() {
    viewModelScope.launch {
      _navigateToTMDBAuth.send(Unit)
    }
  }

  fun logoutDialog() {
    _viewState.update {
      it.copy(
        alertDialogUiState = AlertDialogUiState(
          title = UIText.ResourceText(R.string.feature_settings_logout),
          text = UIText.ResourceText(
            R.string.feature_settings_currently_login_dialog_summary,
            (it.tmdbAccount as? TMDBAccount.LoggedIn)?.accountDetails?.username ?: "",
          ),
        ),
      )
    }
  }

  fun confirmLogout() {
    viewModelScope.launch {
      logoutUseCase.invoke(Unit)
        .onSuccess {
          _viewState.update {
            it.copy(
              tmdbAccount = TMDBAccount.Anonymous,
              alertDialogUiState = null,
            )
          }
        }
        .onFailure { error ->
          ErrorHandler.create(error) {
            on(401) {
              _viewState.update {
                it.copy(
                  tmdbAccount = TMDBAccount.Anonymous,
                  alertDialogUiState = null,
                )
              }
            }
          }
        }
    }
  }

  fun dismissLogoutDialog() {
    _viewState.update {
      it.copy(alertDialogUiState = null)
    }
  }
}
