package com.divinelink.feature.home.customize

import com.divinelink.core.model.home.HomeSectionInfo

data class CustomizeDashboardUiState(
  val sections: List<HomeSectionInfo>,
  val hiddenSections: Set<HomeSectionInfo>,
) {
  companion object {
    val initial = CustomizeDashboardUiState(
      sections = emptyList(),
      hiddenSections = emptySet(),
    )
  }
}
