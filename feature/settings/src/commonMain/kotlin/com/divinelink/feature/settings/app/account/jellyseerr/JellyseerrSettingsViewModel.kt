@file:Suppress("LongMethod")

package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.commons.onError
import com.divinelink.core.domain.jellyseerr.GetJellyseerrProfileUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.UIText
import com.divinelink.core.model.Username
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_could_not_connect
import com.divinelink.feature.settings.resources.feature_settings_invalid_credentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JellyseerrSettingsViewModel(
  private val logoutJellyseerrUseCase: LogoutJellyseerrUseCase,
  getJellyseerrProfileUseCase: GetJellyseerrProfileUseCase,
  private val loginJellyseerrUseCase: LoginJellyseerrUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(JellyseerrSettingsUiState.initial())
  val uiState: StateFlow<JellyseerrSettingsUiState> = _uiState

  init {
    viewModelScope.launch {
      getJellyseerrProfileUseCase.invoke(true)
        .distinctUntilChanged()
        .collect { result ->
          result.onSuccess {
            val accountDetailsResult = result.data
            if (accountDetailsResult.profile == null) {
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
                    accountDetails = accountDetailsResult.profile!!,
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
              } ?: UIText.ResourceText(UiString.core_ui_error_retry),
            )
          }
        }
    }
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
            result.onFailure { error ->
              val text = when (error) {
                is AppException.Unauthorized -> UIText.ResourceText(
                  Res.string.feature_settings_invalid_credentials,
                )
                is AppException.Forbidden -> UIText.ResourceText(
                  Res.string.feature_settings_invalid_credentials,
                )
                is AppException.Offline -> UIText.ResourceText(
                  Res.string.feature_settings_could_not_connect,
                )
                is AppException.SocketTimeout -> UIText.ResourceText(
                  Res.string.feature_settings_could_not_connect,
                )
                else -> error.message?.let { message ->
                  UIText.StringText(message)
                } ?: UIText.ResourceText(UiString.core_ui_error_retry)
              }
              _uiState.setErrorDialog(
                text = text,
                error = error,
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
                address = Address.from(interaction.address),
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
                  username = Username.from(interaction.username),
                ),
              )
            }
            else -> state
          },
        )
      }
      JellyseerrInteraction.OnDismissDialog -> _uiState.update {
        it.copy(dialogState = null)
      }
    }
  }

  private fun onLogout() {
    _uiState.setJellyseerrLoading(true)

    viewModelScope.launch {
      logoutJellyseerrUseCase.invoke(Unit)
        .onSuccess {
          _uiState.setJellyseerrLoading(false)
        }
        .onFailure {
          _uiState.setJellyseerrLoading(false)
        }
    }
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

private fun MutableStateFlow<JellyseerrSettingsUiState>.setErrorDialog(
  text: UIText,
  error: Throwable,
) {
  update { uiState ->
    uiState.copy(
      dialogState = DialogState(
        message = text,
        error = error,
      ),
    )
  }
}
