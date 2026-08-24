package com.divinelink.feature.home.customize

import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.MediaListSection

data class CustomizeDashboardUiState(
  val sections: List<HomeSectionInfo>,
  val hiddenSections: Set<HomeSectionInfo>,
  val displayMessage: DisplayMessage?,
) {
  private val currentSections = sections.map { it.section }
  private val conditionalSections = setOf(MediaListSection.RecentRequests)
  private val expectedDefault = MediaListSection.homeEntries.filter { section ->
    section !in conditionalSections || currentSections.contains(section)
  }
  val restoreToDefaultEnabled = currentSections.toList() != expectedDefault

  companion object {
    val initial = CustomizeDashboardUiState(
      sections = emptyList(),
      hiddenSections = emptySet(),
      displayMessage = null,
    )
  }
}
