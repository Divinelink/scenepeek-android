package com.divinelink.feature.awards.detail

import com.divinelink.core.model.awards.Ceremony
import com.divinelink.core.model.awards.CeremonyCategory
import com.divinelink.core.ui.blankslate.BlankSlateState

data class AwardDetailsUiState(
  val loading: Boolean,
  val error: BlankSlateState?,
  val ceremony: Ceremony,
  val categories: List<CeremonyCategory>,
) {
  companion object {
    fun initial(ceremony: Ceremony) = AwardDetailsUiState(
      ceremony = ceremony,
      loading = true,
      error = null,
      categories = emptyList(),
    )
  }
}
