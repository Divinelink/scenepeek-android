package com.divinelink.core.domain.search

import com.divinelink.core.model.search.SearchEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchStateManager {
  private val _entryPoint = MutableStateFlow(SearchEntryPoint.SEARCH_TAB)
  val entryPoint = _entryPoint.asStateFlow()

  fun updateEntryPoint(newEntryPoint: SearchEntryPoint) {
    _entryPoint.value = newEntryPoint
  }
}
