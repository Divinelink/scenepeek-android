package com.divinelink.feature.details.media.ui.section

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.details.media.DetailSection

@Immutable
data class DetailSectionUiState(
  val section: DetailSection,
  val data: DetailSectionData,
  val page: Int,
) {
  companion object {
    fun initial(section: DetailSection) = DetailSectionUiState(
      section = section,
      page = 1,
      data = DetailSectionData.Loading,
    )
  }
}
