package com.divinelink.feature.home.section

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.media.MediaItem

@Immutable
data class HomeSectionUiState(
  val section: MediaListSection.Home,
  val page: Int,
  val form: HomeForm<MediaItem>,
) {
  companion object {
    fun initial(section: MediaListSection.Home) = HomeSectionUiState(
      section = section,
      page = 0,
      form = HomeForm.Initial,
    )
  }
}
