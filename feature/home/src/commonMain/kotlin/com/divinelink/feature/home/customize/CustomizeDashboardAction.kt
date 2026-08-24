package com.divinelink.feature.home.customize

import com.divinelink.core.model.home.HomeSectionInfo

sealed interface CustomizeDashboardAction {
  data object ConsumeMessage : CustomizeDashboardAction
  data class ToggleVisibility(val section: HomeSectionInfo) : CustomizeDashboardAction
  data class ReorderSections(val newOrder: List<HomeSectionInfo>) : CustomizeDashboardAction
}
