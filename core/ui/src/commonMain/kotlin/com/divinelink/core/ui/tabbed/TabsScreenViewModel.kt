package com.divinelink.core.ui.tabbed

import androidx.lifecycle.ViewModel
import com.divinelink.core.model.tab.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TabsScreenViewModel(
  entryIndex: Int,
  tabs: List<Tab>,
) : ViewModel() {

  private val _uiState: MutableStateFlow<TabsUiState> = MutableStateFlow(
    TabsUiState.initial(entryIndex, tabs),
  )
  val uiState: StateFlow<TabsUiState> = _uiState

  fun onSelectTab(page: Int) {
    _uiState.update { state ->
      state.copy(selectedTabIndex = page)
    }
  }
}
