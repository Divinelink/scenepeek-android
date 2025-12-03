package com.divinelink.core.model.onboarding

sealed class OnboardingAction(open val isComplete: Boolean = false) {
  data class NavigateToTMDBLogin(override val isComplete: Boolean) :
    OnboardingAction(
      isComplete = isComplete,
    )

  data class NavigateToJellyseerrLogin(override val isComplete: Boolean) :
    OnboardingAction(
      isComplete = isComplete,
    )

  data object NavigateToLinkHandling : OnboardingAction()
}
