package com.divinelink.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.onError
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.exception.AppException
import com.divinelink.feature.profile.ui.TMDBAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  private val authRepository: AuthRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Initial,
      isJellyseerrEnabled = false,
    ),
  )
  val uiState: StateFlow<ProfileUiState> = _uiState

  init {
    fetchAccountDetails()

    viewModelScope.launch {
      authRepository.isJellyseerrEnabled.collect { isEnabled ->
        _uiState.update { it.copy(isJellyseerrEnabled = isEnabled) }
      }
    }
  }

  private fun fetchAccountDetails() {
    viewModelScope.launch {
      getAccountDetailsUseCase.invoke(Unit).collect { result ->
        result
          .onSuccess { account ->
            _uiState.update {
              it.copy(
                accountUiState = when (account) {
                  is TMDBAccount.LoggedIn -> TMDBAccountUiState.LoggedIn(account)
                  TMDBAccount.Anonymous -> TMDBAccountUiState.Anonymous
                },
              )
            }
          }
          .onError<AppException.Offline> {
            if (_uiState.value.accountUiState !is TMDBAccountUiState.LoggedIn) {
              _uiState.update {
                it.copy(accountUiState = TMDBAccountUiState.Anonymous)
              }
            }
          }
          .onFailure {
            _uiState.update {
              it.copy(accountUiState = TMDBAccountUiState.Error)
            }
          }
      }
    }
  }
}
