package com.divinelink.core.data.tabs

import com.divinelink.core.model.tab.ProgressTab
import kotlinx.coroutines.flow.StateFlow

interface TabsRepository {
  val nextUpTabs: StateFlow<Set<ProgressTab>>

  suspend fun setNextUpTabs(tabs: Set<ProgressTab>)
}
