package com.divinelink.feature.awards.popular

import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.ui.blankslate.BlankSlateState

data class AwardsUiState(
  val loading: Boolean,
  val error: BlankSlateState?,
  val ceremonies: List<Ceremony>,
) {
  companion object {
    val initial = AwardsUiState(
      loading = true,
      error = null,
      ceremonies = emptyList(),
    )
  }
}
