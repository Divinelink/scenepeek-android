package com.divinelink.feature.awards.category

import com.divinelink.core.model.LoadingUiItem
import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.awards.CeremonyCategory
import com.divinelink.core.ui.blankslate.BlankSlateState

data class AwardCategoryUiState(
  val ceremonyId: String,
  val category: CeremonyCategory,
  val loading: Boolean,
  val error: BlankSlateState?,
  val awards: Map<String, List<LoadingUiItem<AwardNominee>>>,
) {
  companion object {
    fun initial(
      ceremonyId: String,
      category: CeremonyCategory,
    ) = AwardCategoryUiState(
      ceremonyId = ceremonyId,
      category = category,
      loading = true,
      error = null,
      awards = emptyMap(),
    )
  }
}
