package com.divinelink.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.OnboardingAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IntroViewModel(
  private val markOnboardingCompleteUseCase: MarkOnboardingCompleteUseCase,
  private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
  private val getJellyseerrAccountDetailsUseCase: GetJellyseerrAccountDetailsUseCase,
  onboardingManager: OnboardingManager,
) : ViewModel() {

  private val _uiState = MutableStateFlow(OnboardingUiState.initial())
  val uiState: StateFlow<OnboardingUiState> = _uiState

  private val _onNavigateUp = Channel<Unit>()
  val onNavigateUp: Flow<Unit> = _onNavigateUp.receiveAsFlow()

  init {
    combine(
      onboardingManager.isInitialOnboarding,
      onboardingManager.sections,
    ) { isFirstLaunch, sections ->
      _uiState.update { uiState ->
        uiState.copy(
          sections = sections,
          isFirstLaunch = isFirstLaunch,
        )
      }

      if (isFirstLaunch) {
        startAccountObservers()
      }
    }.launchIn(viewModelScope)
  }

  fun onboardingComplete() {
    viewModelScope.launch {
      markOnboardingCompleteUseCase
        .invoke(Unit)
        .map {
          _onNavigateUp.send(Unit)
        }
    }
  }

  private fun startAccountObservers() {
    fetchAccountJob()
    fetchJellyseerrAccountJob()
  }

  private fun fetchAccountJob() {
    viewModelScope.launch {
      getAccountDetailsUseCase.invoke(Unit).collect { result ->
        result.onSuccess {
          _uiState.update { uiState ->
            uiState.copy(
              sections = uiState.sections.map { section ->
                if (
                  section is IntroSection.Feature &&
                  section.action is OnboardingAction.NavigateToTMDBLogin &&
                  it is TMDBAccount.LoggedIn
                ) {
                  section.copy(action = OnboardingAction.NavigateToTMDBLogin(true))
                } else {
                  section
                }
              },
            )
          }
        }
      }
    }
  }

  private fun fetchJellyseerrAccountJob() {
    viewModelScope.launch {
      getJellyseerrAccountDetailsUseCase.invoke(true).collect { result ->
        result.onSuccess { accountResult ->
          if (accountResult.accountDetails == null) return@collect

          _uiState.update { uiState ->
            uiState.copy(
              sections = uiState.sections.map { section ->
                if (
                  section is IntroSection.Feature &&
                  section.action is OnboardingAction.NavigateToJellyseerrLogin
                ) {
                  section.copy(action = OnboardingAction.NavigateToJellyseerrLogin(true))
                } else {
                  section
                }
              },
            )
          }
        }
      }
    }
  }
}
