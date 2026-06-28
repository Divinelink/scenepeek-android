package com.divinelink.feature.awards.category

import com.divinelink.core.model.LoadingUiItem
import com.divinelink.core.model.awards.AwardNominee

sealed interface AwardCategoryAction {
  data object OnRetry : AwardCategoryAction
  data class FetchMediaItem(val nominee: LoadingUiItem<AwardNominee>) : AwardCategoryAction
}
