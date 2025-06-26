package com.divinelink.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.feature.profile.ui.TMDBAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val getAccountDetailsUseCase: GetAccountDetailsUseCase) :
  ViewModel() {

  private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
    ProfileUiState(
      accountUiState = TMDBAccountUiState.Initial,
    ),
  )
  val uiState: StateFlow<ProfileUiState> = _uiState

  init {
    fetchAccountDetails()
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
                  TMDBAccount.NotLoggedIn -> TMDBAccountUiState.NotLoggedIn
                },
              )
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
