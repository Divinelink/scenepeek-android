package com.divinelink.feature.credits.ui

import com.divinelink.feature.credits.Res
import com.divinelink.feature.credits.feature_credits_cast
import com.divinelink.feature.credits.feature_credits_crew
import org.jetbrains.compose.resources.StringResource

sealed class CreditsTab(
  val titleRes: StringResource,
  open var size: Int,
) {
  data class Cast(override var size: Int) : CreditsTab(Res.string.feature_credits_cast, size)
  data class Crew(override var size: Int) : CreditsTab(Res.string.feature_credits_crew, size)
}
