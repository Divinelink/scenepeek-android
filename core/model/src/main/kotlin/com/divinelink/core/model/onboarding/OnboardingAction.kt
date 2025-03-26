package com.divinelink.core.model.onboarding

import com.divinelink.core.model.R
import com.divinelink.core.model.UIText

sealed class OnboardingAction(
  val actionText: UIText,
  val completedActionText: UIText? = null,
  open val isComplete: Boolean = false,
) {
  data class NavigateToTMDBLogin(override val isComplete: Boolean) :
    OnboardingAction(
      actionText = UIText.ResourceText(R.string.core_model_onboarding_tmdb_page_action),
      completedActionText = UIText.ResourceText(
        R.string.core_model_onboarding_successfully_connected,
      ),
      isComplete = isComplete,
    )

  data class NavigateToJellyseerrLogin(override val isComplete: Boolean) :
    OnboardingAction(
      actionText = UIText.ResourceText(R.string.core_model_onboarding_jellyseerr_page_action),
      completedActionText = UIText.ResourceText(
        R.string.core_model_onboarding_successfully_connected,
      ),
    )

  data object NavigateToLinkHandling : OnboardingAction(
    actionText = UIText.ResourceText(R.string.core_model_onboarding_link_handling_action),
  )
}
