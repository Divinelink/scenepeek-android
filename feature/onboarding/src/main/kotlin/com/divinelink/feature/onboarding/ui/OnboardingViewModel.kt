package com.divinelink.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import com.divinelink.feature.onboarding.OnboardingAction
import com.divinelink.feature.onboarding.manager.OnboardingManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
  private val markOnboardingCompleteUseCase: MarkOnboardingCompleteUseCase,
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  private val getJellyseerrAccountDetailsUseCase: GetJellyseerrAccountDetailsUseCase,
  private val onboardingManager: OnboardingManager,
) : ViewModel() {

  private val _uiState = MutableStateFlow(OnboardingUiState.initial())
  val uiState: StateFlow<OnboardingUiState> = _uiState

  private val _onNavigateUp = Channel<Unit>()
  val onNavigateUp: Flow<Unit> = _onNavigateUp.receiveAsFlow()

  init {
    viewModelScope.launch {
      getAccountDetailsUseCase.invoke(Unit).collect { result ->
        result.onSuccess {
          _uiState.update { uiState ->
            uiState.copy(
              pages = uiState.pages.map { page ->
                if (page.action is OnboardingAction.NavigateToTMDBLogin) {
                  page.copy(action = OnboardingAction.NavigateToTMDBLogin(true))
                } else {
                  page
                }
              },
            )
          }
        }
      }
    }

    viewModelScope.launch {
      getJellyseerrAccountDetailsUseCase.invoke(false).collect { result ->
        result.onSuccess { accountDetails ->
          if (accountDetails == null) return@collect

          _uiState.update { uiState ->
            uiState.copy(
              pages = uiState.pages.map { page ->
                if (page.action is OnboardingAction.NavigateToJellyseerrLogin) {
                  page.copy(action = OnboardingAction.NavigateToJellyseerrLogin(true))
                } else {
                  page
                }
              },
            )
          }
        }
      }
    }

    viewModelScope.launch {
      onboardingManager.onboardingPages.collect {
        _uiState.update { uiState ->
          uiState.copy(pages = it)
        }
      }
    }
  }

  fun onboardingComplete() {
    viewModelScope.launch {
      markOnboardingCompleteUseCase.invoke(Unit)
      _onNavigateUp.send(Unit)
    }
  }
}
