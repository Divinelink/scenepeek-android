package com.divinelink.feature.details.media.ui.section

import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.blankslate.BlankSlateState

sealed interface DetailSectionData {
  data object Loading : DetailSectionData

  data class Error(val state: BlankSlateState) : DetailSectionData

  data class Media(
    val paginationData: Map<Int, List<MediaItem.Media>>,
  ) : DetailSectionData {
    val data = paginationData.values.flatten().distinctBy { it.uniqueIdentifier }
    val isEmpty: Boolean = data.isEmpty()
  }

  data class Reviews(val items: List<Review>) : DetailSectionData
}
