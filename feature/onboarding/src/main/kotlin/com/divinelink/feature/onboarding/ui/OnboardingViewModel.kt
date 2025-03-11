package com.divinelink.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(OnboardingUiState.initial())
  val uiState: StateFlow<OnboardingUiState> = _uiState

  init {

  }
}
