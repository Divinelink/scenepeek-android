package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.discover.ui.DiscoverSectionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DiscoverSectionsViewModel(
  route: Navigation.Discover,
  clock: ClockProvider,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverSectionsUiState> = MutableStateFlow(
    DiscoverSectionsUiState.initial(
      route,
      clock.nowDT.date.toString(),
    ),
  )
  val uiState: StateFlow<DiscoverSectionsUiState> = _uiState

  fun onSelectTab(page: Int) {
    _uiState.update { state ->
      state.copy(selectedTabIndex = page)
    }
  }
}
