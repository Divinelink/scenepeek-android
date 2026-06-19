package com.divinelink.feature.awards.detail

import com.divinelink.core.model.awards.CeremonyCategory

sealed interface AwardDetailsAction {
  data class OnCategoryClick(
    val ceremonyId: String,
    val category: CeremonyCategory,
  ) : AwardDetailsAction
  data object OnRetry : AwardDetailsAction
}
