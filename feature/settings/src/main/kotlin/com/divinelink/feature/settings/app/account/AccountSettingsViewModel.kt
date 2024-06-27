package com.divinelink.feature.settings.app.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.CreateRequestTokenUseCase
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.domain.session.LogoutUseCase
import com.divinelink.core.domain.session.ObserveSessionUseCase
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.model.jellyseerr.loginParams
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrInteraction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import com.divinelink.core.ui.R as uiR

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
  private val createRequestTokenUseCase: CreateRequestTokenUseCase,
  private val observeSessionUseCase: ObserveSessionUseCase,
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  private val logoutUseCase: LogoutUseCase,
  private val logoutJellyseerrUseCase: LogoutJellyseerrUseCase,
  getJellyseerrDetailsUseCase: GetJellyseerrDetailsUseCase,
  private val loginJellyseerrUseCase: LoginJellyseerrUseCase,
) : ViewModel() {

  private val _viewState = MutableStateFlow(AccountSettingsViewState.initial())
  val viewState: StateFlow<AccountSettingsViewState> = _viewState

  init {
    observeSession()

    getJellyseerrDetailsUseCase.invoke(Unit)
      .distinctUntilChanged()
      .onEach { result ->
        println("Jellyseerr details fetched : $result")
        result.onSuccess {
          _viewState.update {
            it.copy(
              jellyseerrState = JellyseerrState.LoggedIn(
                loginData = result.data,
                isLoading = false,
              ),
            )
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
        .onSuccess { response ->
          _viewState.update {
            it.copy(
              requestToken = response,
              navigateToWebView = true,
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
              " ${it.accountDetails?.username}. Are you sure you want to logout?",
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

  fun onWebViewScreenNavigated() {
    _viewState.update {
      it.copy(navigateToWebView = null)
    }
  }

  fun onJellyseerrInteraction(interaction: JellyseerrInteraction) {
    when (interaction) {
      JellyseerrInteraction.OnLoginClick -> {
        loginJellyseerrUseCase.invoke(viewState.value.jellyseerrState.loginParams)
          .onStart {
            _viewState.setJellyseerrLoading(true)
          }
          .onCompletion {
            _viewState.setJellyseerrLoading(false)
          }
          .onEach { result ->
            result.onSuccess {
              _viewState.update {
                it.copy(
                  jellyseerrState = JellyseerrState.LoggedIn(
                    loginData = result.data,
                    isLoading = false,
                  ),
                )
              }
            }.onFailure { error ->
              ErrorHandler.create(error)
                .on(401) {
                  _viewState.setSnackbarMessage(
                    UIText.ResourceText(R.string.feature_settings_invalid_credentials),
                  )
                }
                .on<UnknownHostException> {
                  _viewState.setSnackbarMessage(
                    UIText.ResourceText(R.string.feature_settings_could_not_connect),
                  )
                }
                .on<ConnectException> {
                  _viewState.setSnackbarMessage(
                    UIText.ResourceText(R.string.feature_settings_could_not_connect),
                  )
                }
                .otherwise {
                  _viewState.setSnackbarMessage(UIText.ResourceText(uiR.string.core_ui_error_retry))
                }
                .handle()
            }
          }.launchIn(viewModelScope)
      }
      JellyseerrInteraction.OnLogoutClick -> {
        logoutJellyseerrUseCase.invoke(Unit)
          .onStart {
            _viewState.setJellyseerrLoading(true)
          }
          .onCompletion {
            _viewState.setJellyseerrLoading(false)
          }
          .onEach { result ->
            result.onSuccess {
              _viewState.update {
                it.copy(jellyseerrState = JellyseerrState.Initial(isLoading = false))
              }
            }
          }
          .launchIn(viewModelScope)
      }
      is JellyseerrInteraction.OnAddressChange -> {
        _viewState.update {
          it.copy(
            jellyseerrState = when (val state = it.jellyseerrState) {
              is JellyseerrState.Initial -> state.copy(address = interaction.address)
              is JellyseerrState.LoggedIn -> state.copy(
                loginData = state.loginData.copy(address = interaction.address),
              )
            },
          )
        }
      }

      is JellyseerrInteraction.OnSelectLoginMethod -> {
        _viewState.update {
          it.copy(
            jellyseerrState = when (val state = it.jellyseerrState) {
              is JellyseerrState.Initial -> {
                if (state.preferredOption == interaction.signInMethod) {
                  state.copy(preferredOption = null)
                } else {
                  state.copy(preferredOption = interaction.signInMethod)
                }
              }
              else -> state
            },
          )
        }
      }
      is JellyseerrInteraction.OnPasswordChange -> {
        _viewState.update {
          it.copy(
            jellyseerrState = when (val state = it.jellyseerrState) {
              is JellyseerrState.Initial -> {
                if (state.preferredOption == JellyseerrLoginMethod.JELLYFIN) {
                  state.copy(
                    jellyfinLogin = state.jellyfinLogin.copy(
                      password = Password(interaction.password),
                    ),
                  )
                } else {
                  state.copy(
                    jellyseerrLogin = state.jellyseerrLogin.copy(
                      password = Password(interaction.password),
                    ),
                  )
                }
              }
              else -> state
            },
          )
        }
      }
      is JellyseerrInteraction.OnUsernameChange -> {
        _viewState.update {
          it.copy(
            jellyseerrState = when (val state = it.jellyseerrState) {
              is JellyseerrState.Initial -> {
                if (state.preferredOption == JellyseerrLoginMethod.JELLYFIN) {
                  state.copy(
                    jellyfinLogin = state.jellyfinLogin.copy(
                      username = Username(interaction.username),
                    ),
                  )
                } else {
                  state.copy(
                    jellyseerrLogin = state.jellyseerrLogin.copy(
                      username = Username(interaction.username),
                    ),
                  )
                }
              }
              else -> state
            },
          )
        }
      }
    }
  }

  fun dismissSnackbar() {
    _viewState.update {
      it.copy(snackbarMessage = null)
    }
  }
}

private fun MutableStateFlow<AccountSettingsViewState>.setJellyseerrLoading(loading: Boolean) {
  update { uiState ->
    when (uiState.jellyseerrState) {
      is JellyseerrState.LoggedIn -> uiState.copy(
        jellyseerrState = uiState.jellyseerrState.copy(
          isLoading = loading,
        ),
      )
      is JellyseerrState.Initial -> uiState.copy(
        jellyseerrState = uiState.jellyseerrState.copy(
          isLoading = loading,
        ),
      )
    }
  }
}

private fun MutableStateFlow<AccountSettingsViewState>.setSnackbarMessage(text: UIText) {
  update { uiState ->
    uiState.copy(
      snackbarMessage = SnackbarMessage.from(text = text),
    )
  }
}
