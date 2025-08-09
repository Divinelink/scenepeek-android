package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.data
import com.divinelink.core.commons.domain.onError
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.model.Password
import com.divinelink.core.model.UIText
import com.divinelink.core.model.Username
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import com.divinelink.core.ui.R as uiR

class JellyseerrSettingsViewModel(
  private val logoutJellyseerrUseCase: LogoutJellyseerrUseCase,
  getJellyseerrDetailsUseCase: GetJellyseerrAccountDetailsUseCase,
  private val loginJellyseerrUseCase: LoginJellyseerrUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(JellyseerrSettingsUiState.initial())
  val uiState: StateFlow<JellyseerrSettingsUiState> = _uiState

  init {
    getJellyseerrDetailsUseCase.invoke(true)
      .distinctUntilChanged()
      .onEach { result ->
        result.onSuccess {
          val accountDetailsResult = result.data
          if (accountDetailsResult.accountDetails == null) {
            _uiState.update {
              it.copy(
                jellyseerrState = JellyseerrState.Login(
                  isLoading = false,
                  loginData = JellyseerrLoginData.prefilled(
                    address = accountDetailsResult.address,
                  ),
                ),
              )
            }
          } else {
            _uiState.update {
              it.copy(
                jellyseerrState = JellyseerrState.LoggedIn(
                  accountDetails = accountDetailsResult.accountDetails!!,
                  isLoading = false,
                  address = accountDetailsResult.address,
                ),
              )
            }
          }
        }.onError<AppException.Unauthorized> {
          onLogout()
        }.onFailure { throwable ->
          _uiState.setSnackbarMessage(
            throwable.message?.let { message ->
              UIText.StringText(message)
            } ?: UIText.ResourceText(uiR.string.core_ui_error_retry),
          )
        }
      }.launchIn(viewModelScope)
  }

  fun dismissSnackbar() {
    _uiState.update {
      it.copy(snackbarMessage = null)
    }
  }

  fun onJellyseerrInteraction(interaction: JellyseerrInteraction) {
    when (interaction) {
      JellyseerrInteraction.OnLoginClick -> {
        if (uiState.value.jellyseerrState !is JellyseerrState.Login) return

        loginJellyseerrUseCase.invoke(uiState.value.jellyseerrState.loginData)
          .onStart {
            _uiState.setJellyseerrLoading(true)
          }
          .onCompletion {
            _uiState.setJellyseerrLoading(false)
          }.onEach { result ->
            result
              .onError<AppException.Unauthorized> {
                _uiState.setSnackbarMessage(
                  UIText.ResourceText(R.string.feature_settings_invalid_credentials),
                )
              }.onError<AppException.Forbidden> {
                _uiState.setSnackbarMessage(
                  UIText.ResourceText(R.string.feature_settings_invalid_credentials),
                )
              }.onError<AppException.Offline> {
                _uiState.setSnackbarMessage(
                  UIText.ResourceText(R.string.feature_settings_could_not_connect),
                )
              }.onError<AppException.SocketTimeout> {
                _uiState.setSnackbarMessage(
                  UIText.ResourceText(R.string.feature_settings_could_not_connect),
                )
              }.onFailure {
                _uiState.setSnackbarMessage(
                  it.message?.let { message ->
                    UIText.StringText(message)
                  } ?: UIText.ResourceText(uiR.string.core_ui_error_retry),
                )
              }
          }.launchIn(viewModelScope)
      }
      JellyseerrInteraction.OnLogoutClick -> onLogout()
      is JellyseerrInteraction.OnAddressChange -> _uiState.update {
        it.copy(
          jellyseerrState = when (val state = it.jellyseerrState) {
            is JellyseerrState.Login -> state.copy(
              loginData = uiState.value.jellyseerrState.loginData.copy(
                address = interaction.address,
              ),
            )
            else -> state
          },
        )
      }

      is JellyseerrInteraction.OnSelectLoginMethod -> _uiState.update {
        it.copy(
          jellyseerrState = when (val state = it.jellyseerrState) {
            is JellyseerrState.Login -> {
              state.copy(
                loginData = uiState.value.jellyseerrState.loginData.copy(
                  authMethod = interaction.signInMethod,
                ),
              )
            }
            else -> state
          },
        )
      }

      is JellyseerrInteraction.OnPasswordChange -> _uiState.update {
        it.copy(
          jellyseerrState = when (val state = it.jellyseerrState) {
            is JellyseerrState.Login -> {
              state.copy(
                loginData = state.loginData.copy(
                  password = Password(interaction.password),
                ),
              )
            }
            else -> state
          },
        )
      }

      is JellyseerrInteraction.OnUsernameChange -> _uiState.update {
        it.copy(
          jellyseerrState = when (val state = it.jellyseerrState) {
            is JellyseerrState.Login -> {
              state.copy(
                loginData = state.loginData.copy(
                  username = Username(interaction.username),
                ),
              )
            }
            else -> state
          },
        )
      }
    }
  }

  private fun onLogout() {
    logoutJellyseerrUseCase.invoke(Unit)
      .onStart {
        _uiState.setJellyseerrLoading(true)
      }
      .onCompletion {
        _uiState.setJellyseerrLoading(false)
      }
      .launchIn(viewModelScope)
  }
}

private fun MutableStateFlow<JellyseerrSettingsUiState>.setJellyseerrLoading(loading: Boolean) {
  update { uiState ->
    when (uiState.jellyseerrState) {
      is JellyseerrState.LoggedIn -> uiState.copy(
        jellyseerrState = uiState.jellyseerrState.copy(
          isLoading = loading,
        ),
      )
      is JellyseerrState.Login -> uiState.copy(
        jellyseerrState = uiState.jellyseerrState.copy(
          isLoading = loading,
        ),
      )
    }
  }
}

private fun MutableStateFlow<JellyseerrSettingsUiState>.setSnackbarMessage(text: UIText) {
  update { uiState ->
    uiState.copy(
      snackbarMessage = SnackbarMessage.from(text = text),
    )
  }
}
