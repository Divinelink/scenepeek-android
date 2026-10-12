package com.divinelink.core.ui.tabbed

import com.divinelink.core.model.tab.Tab

data class TabsUiState(
  val selectedTab: Tab,
  val tabs: Set<Tab>,
) {
  val selectedTabIndex = tabs.indexOf(selectedTab)

  companion object {
    fun initial(
      selectedTab: Tab,
      tabs: Set<Tab>,
    ): TabsUiState = TabsUiState(
      selectedTab = selectedTab,
      tabs = tabs,
    )
  }
}
