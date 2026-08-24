package com.divinelink.feature.home.customize

import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.home.HomeSectionInfo

data class CustomizeDashboardUiState(
  val sections: List<HomeSectionInfo>,
  val hiddenSections: Set<HomeSectionInfo>,
  val displayMessage: DisplayMessage?,
) {
  companion object {
    val initial = CustomizeDashboardUiState(
      sections = emptyList(),
      hiddenSections = emptySet(),
      displayMessage = null,
    )
  }
}
