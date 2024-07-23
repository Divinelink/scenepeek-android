package com.divinelink.feature.credits.ui

import androidx.annotation.StringRes
import com.divinelink.feature.credits.R

sealed class CreditsTab(
  @StringRes val titleRes: Int,
  open var size: Int,
) {
  data class Cast(override var size: Int) : CreditsTab(R.string.feature_credits_cast, size)
  data class Crew(override var size: Int) : CreditsTab(R.string.feature_credits_crew, size)
}
