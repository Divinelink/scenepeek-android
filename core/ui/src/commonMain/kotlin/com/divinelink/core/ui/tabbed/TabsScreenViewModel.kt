package com.divinelink.core.ui.tabbed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.tabs.TabsRepository
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.model.tab.TabsScreenEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class TabsScreenViewModel(
  entryPoint: TabsScreenEntryPoint,
  repository: TabsRepository,
  selectedTab: Tab,
  tabs: Set<Tab>,
) : ViewModel() {

  private val _uiState: MutableStateFlow<TabsUiState> = MutableStateFlow(
    TabsUiState.initial(selectedTab, tabs),
  )
  val uiState: StateFlow<TabsUiState> = _uiState

  init {
    when (entryPoint) {
      TabsScreenEntryPoint.NextUp ->
        repository
          .nextUpTabs
          .onEach { newTabs ->
            _uiState.update { state ->
              val oldIndex = state.tabs.indexOf(state.selectedTab)

              val newSelectedTab = when {
                state.selectedTab in newTabs -> state.selectedTab
                else -> newTabs.elementAt(oldIndex.coerceIn(0, newTabs.indices.last))
              }

              state.copy(
                tabs = newTabs.sortedBy { it.order }.toSet(),
                selectedTab = newSelectedTab,
              )
            }
          }.launchIn(viewModelScope)
    }
  }

  fun onSelectTab(index: Int) {
    val tab = _uiState.value.tabs.elementAt(index)

    _uiState.update { state ->
      state.copy(selectedTab = tab)
    }
  }
}
