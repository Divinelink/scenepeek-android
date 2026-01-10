package com.divinelink.feature.home

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.Filter

@Immutable
data class HomeUiState(
  val filters: List<Filter>,
  val filteredResults: MediaSection?,
  val pages: Map<HomeSection, Int>,
  val forms: Map<HomeSection, HomeForm<MediaItem>>,
  val sections: List<HomeSectionInfo>,
  val error: BlankSlateState?,
) {
  companion object {
    fun initial(sections: List<HomeSectionInfo>) = HomeUiState(
      filters = HomeFilter.entries.map { it.filter },
      filteredResults = null,
      pages = emptyMap(),
      sections = sections,
      forms = sections.buildForms(),
      error = null,
    )

    private fun List<HomeSectionInfo>.buildForms(): Map<HomeSection, HomeForm<MediaItem>> =
      associate {
        it.section to HomeForm.Initial
      }
  }
}

enum class HomeFilter(val filter: Filter) {
  Liked(Filter(name = "Liked By You", isSelected = false)),
}
