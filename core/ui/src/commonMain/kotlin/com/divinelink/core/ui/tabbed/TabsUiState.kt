package com.divinelink.core.ui.tabbed

import com.divinelink.core.model.tab.Tab

data class TabsUiState(
  val selectedTabIndex: Int,
  val tabs: List<Tab>,
) {
  companion object {
    fun initial(
      index: Int,
      tabs: List<Tab>,
    ): TabsUiState {
      return TabsUiState(
        selectedTabIndex = index,
        tabs = tabs,
      )
    }
  }
}
