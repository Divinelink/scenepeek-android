package com.divinelink.feature.home

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.filter.HomeFilter.Favorites
import com.divinelink.core.model.filter.HomeFilter.TopRated
import com.divinelink.core.model.filter.SelectableFilter
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.blankslate.BlankSlateState

@Immutable
data class HomeUiState(
  val filters: List<SelectableFilter>,
  val pages: Map<HomeSection, Int>,
  val forms: Map<HomeSection, HomeForm<MediaItem>>,
  val sections: List<HomeSectionInfo>,
  val error: BlankSlateState?,
) {
  companion object {
    fun initial(sections: List<HomeSectionInfo>) = HomeUiState(
      filters = listOf(
        Favorites(isSelected = false),
        TopRated(isSelected = false),
      ),
      pages = emptyMap(),
      sections = sections,
      forms = sections.buildForms(),
      error = null,
    )

    fun List<HomeSectionInfo>.buildForms(): Map<HomeSection, HomeForm<MediaItem>> =
      associate {
        it.section to HomeForm.Initial
      }
  }
}
