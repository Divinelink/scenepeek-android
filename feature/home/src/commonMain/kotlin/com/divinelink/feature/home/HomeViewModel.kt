package com.divinelink.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.search.SearchEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class HomeViewModel(
  preferencesRepository: PreferencesRepository,
  authRepository: AuthRepository,
  private val searchStateManager: SearchStateManager,
) : ViewModel() {

  private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.initial)
  val uiState: StateFlow<HomeUiState> = _uiState

  init {
    preferencesRepository
      .homeSections
      .withFeatureFlags(seerrEnabled = authRepository.isJellyseerrEnabled)
      .distinctUntilChanged()
      .onEach { sections ->
        val sections = sections
          .getVisibleSections()
          .buildSections()

        _uiState.update { state ->
          state.copy(
            sections = sections,
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun onNavigateToSearch() {
    searchStateManager.updateEntryPoint(SearchEntryPoint.HOME)
  }
}
