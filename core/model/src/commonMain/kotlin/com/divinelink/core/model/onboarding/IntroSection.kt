package com.divinelink.core.model.onboarding

import com.divinelink.core.model.Res
import com.divinelink.core.model.UIText
import com.divinelink.core.model.core_model_onboarding_added_header
import com.divinelink.core.model.core_model_onboarding_features_headers
import com.divinelink.core.model.core_model_onboarding_fixed_header
import org.jetbrains.compose.resources.DrawableResource

sealed interface IntroSection {

  data class Header(
    val title: UIText,
    val description: UIText? = null,
  ) : IntroSection

  data class WhatsNew(val version: String) : IntroSection

  data object Divider : IntroSection
  data object Spacer : IntroSection

  sealed class SecondaryHeader(open val title: UIText) : IntroSection {
    data object Added : SecondaryHeader(
      title = UIText.ResourceText(Res.string.core_model_onboarding_added_header),
    )

    data object Fixed : SecondaryHeader(
      title = UIText.ResourceText(Res.string.core_model_onboarding_fixed_header),
    )

    data object Features : SecondaryHeader(
      title = UIText.ResourceText(Res.string.core_model_onboarding_features_headers),
    )
  }

  data class Feature(
    val title: UIText,
    val description: UIText,
    val image: DrawableResource? = null,
    val action: OnboardingAction? = null,
  ) : IntroSection {
    val isClickable: Boolean
      get() = action != null && !action.isComplete
  }

  data class Text(val description: UIText) : IntroSection

  data object GetStartedButton : IntroSection
}
