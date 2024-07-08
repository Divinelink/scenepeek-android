package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.jellyseerr.GetJellyseerrDetailsUseCase
import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.model.jellyseerr.loginParams
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import com.divinelink.core.ui.R as uiR

@HiltViewModel
class JellyseerrSettingsViewModel @Inject constructor(
  private val logoutJellyseerrUseCase: LogoutJellyseerrUseCase,
  getJellyseerrDetailsUseCase: GetJellyseerrDetailsUseCase,
  private val loginJellyseerrUseCase: LoginJellyseerrUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(JellyseerrSettingsUiState.initial())
  val uiState: StateFlow<JellyseerrSettingsUiState> = _uiState

  init {
    getJellyseerrDetailsUseCase.invoke(Unit)
      .distinctUntilChanged()
      .onEach { result ->
        println("Jellyseerr details fetched : $result")
        result.onSuccess {
          _uiState.update {
            it.copy(
              jellyseerrState = JellyseerrState.LoggedIn(
                accountDetails = result.data,
                isLoading = false,
              ),
            )
          }
        }.onFailure {
          _uiState.update {
            it.copy(
              jellyseerrState = JellyseerrState.Initial(
                address = "",
                isLoading = false,
              ),
            )
          }
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
        loginJellyseerrUseCase.invoke(uiState.value.jellyseerrState.loginParams)
          .onStart {
            _uiState.setJellyseerrLoading(true)
          }
          .onCompletion {
            _uiState.setJellyseerrLoading(false)
          }
          .onEach { result ->
            result.onSuccess {
              _uiState.update {
                it.copy(
                  jellyseerrState = JellyseerrState.LoggedIn(
                    accountDetails = result.data,
                    isLoading = false,
                  ),
                )
              }
            }.onFailure { error ->
              ErrorHandler.create(error)
                .on(401) {
                  _uiState.setSnackbarMessage(
                    UIText.ResourceText(R.string.feature_settings_invalid_credentials),
                  )
                }
                .on<UnknownHostException> {
                  _uiState.setSnackbarMessage(
                    UIText.ResourceText(R.string.feature_settings_could_not_connect),
                  )
                }
                .on<ConnectException> {
                  _uiState.setSnackbarMessage(
                    UIText.ResourceText(R.string.feature_settings_could_not_connect),
                  )
                }
                .otherwise {
                  _uiState.setSnackbarMessage(
                    UIText.ResourceText(uiR.string.core_ui_error_retry),
                  )
                }
                .handle()
            }
          }.launchIn(viewModelScope)
      }
      JellyseerrInteraction.OnLogoutClick -> {
        logoutJellyseerrUseCase.invoke(Unit)
          .onStart {
            _uiState.setJellyseerrLoading(true)
          }
          .onCompletion {
            _uiState.setJellyseerrLoading(false)
          }
          .onEach { result ->
            result.onSuccess {
              _uiState.update {
                it.copy(
                  jellyseerrState = JellyseerrState.Initial(
                    address = result.data,
                    isLoading = false,
                  ),
                )
              }
            }.onFailure {
              _uiState.setSnackbarMessage(UIText.ResourceText(uiR.string.core_ui_error_retry))
            }
          }
          .launchIn(viewModelScope)
      }
      is JellyseerrInteraction.OnAddressChange -> {
        _uiState.update {
          it.copy(
            jellyseerrState = when (val state = it.jellyseerrState) {
              is JellyseerrState.Initial -> state.copy(address = interaction.address)
              else -> state
            },
          )
        }
      }

      is JellyseerrInteraction.OnSelectLoginMethod -> {
        _uiState.update {
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
        _uiState.update {
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
        _uiState.update {
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
}

private fun MutableStateFlow<JellyseerrSettingsUiState>.setJellyseerrLoading(loading: Boolean) {
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
      JellyseerrState.Loading -> uiState
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
