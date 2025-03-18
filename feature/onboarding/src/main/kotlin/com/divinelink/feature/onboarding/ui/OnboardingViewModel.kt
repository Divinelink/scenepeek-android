package com.divinelink.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
  private val markOnboardingCompleteUseCase: MarkOnboardingCompleteUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(OnboardingUiState.initial())
  val uiState: StateFlow<OnboardingUiState> = _uiState

  fun onboardingComplete() {
    viewModelScope.launch {
      markOnboardingCompleteUseCase.invoke(Unit)
    }
  }
}
