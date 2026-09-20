package com.divinelink.feature.details.media.ui.section

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.details.media.DetailSection
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.blankslate.BlankSlateState

@Immutable
data class DetailSectionUiState(
  val section: DetailSection,
  val data: DetailSectionData,
  val page: Int,
) {
  companion object {
    fun initial(
      section: DetailSection,
    ) = DetailSectionUiState(
      section = section,
      page = 1,
      data = DetailSectionData.Loading,
    )
  }
}

sealed interface DetailSectionData {
  data object Loading : DetailSectionData

  data class Error(val state: BlankSlateState) : DetailSectionData

  data class Media(
    val paginationData: Map<Int, List<MediaItem.Media>>,
  ) : DetailSectionData {
    val media = paginationData.values.flatten().distinctBy { it.uniqueIdentifier }
    val isEmpty: Boolean = media.isEmpty()
  }

  data class Reviews(val items: List<Review>) : DetailSectionData
}
