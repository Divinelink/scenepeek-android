package com.divinelink.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.feature.onboarding.manager.OnboardingPages
import com.divinelink.feature.onboarding.manager.OnboardingPages.jellyseerrPage
import com.divinelink.feature.onboarding.manager.OnboardingPages.tmdbPage
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

  fun onPageScroll(index: Int) {
    _uiState.update { uiState ->
      uiState.copy(selectedPageIndex = index)
    }

    viewModelScope.launch {
      if (onboardingManager.isInitialOnboarding.first()) {
        val tmdbIndex = OnboardingPages.initialPages.indexOf(tmdbPage)
        val jellyseerrIndex = OnboardingPages.initialPages.indexOf(jellyseerrPage)

        if (index == tmdbIndex && !uiState.value.startedJobs.contains(tmdbPage.tag)) {
          _uiState.update { uiState ->
            uiState.copy(startedJobs = uiState.startedJobs + tmdbPage.tag)
          }
          fetchAccountJob.invoke()
        } else if (
          index == jellyseerrIndex && !uiState.value.startedJobs.contains(jellyseerrPage.tag)
        ) {
          _uiState.update { uiState ->
            uiState.copy(startedJobs = uiState.startedJobs + jellyseerrPage.tag)
          }
          fetchJellyseerrAccountJob.invoke()
        }
      }
    }
  }

  private val fetchAccountJob: () -> Job = {
    viewModelScope.launch {
      getAccountDetailsUseCase.invoke(Unit).collect { result ->
        result.onSuccess {
          _uiState.update { uiState ->
            uiState.copy(
              pages = uiState.pages.map { page ->
                if (
                  page.action is OnboardingAction.NavigateToTMDBLogin &&
                  it is TMDBAccount.LoggedIn
                ) {
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
  }

  private val fetchJellyseerrAccountJob: () -> Job = {
    viewModelScope.launch {
      getJellyseerrAccountDetailsUseCase.invoke(true).collect { result ->
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
  }
}
