package com.divinelink.core.data.tabs

import com.divinelink.core.model.tab.ProgressTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProdTabsRepository : TabsRepository {

  private val _nextUpTabs: MutableStateFlow<Set<ProgressTab>> = MutableStateFlow(
    ProgressTab.entries,
  )
  override val nextUpTabs: StateFlow<Set<ProgressTab>> = _nextUpTabs

  override suspend fun setNextUpTabs(tabs: Set<ProgressTab>) {
    _nextUpTabs.emit(tabs)
  }
}
