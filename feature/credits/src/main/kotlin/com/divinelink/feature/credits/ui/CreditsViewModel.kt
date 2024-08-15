package com.divinelink.feature.credits.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.navigation.arguments.CreditsNavArguments
import com.divinelink.feature.credits.screens.destinations.CreditsScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class CreditsViewModel(
  fetchCreditsUseCase: FetchCreditsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args: CreditsNavArguments = CreditsScreenDestination.argsFrom(savedStateHandle)

  private val _uiState: MutableStateFlow<CreditsUiState> = MutableStateFlow(
    CreditsUiState.initial(),
  )

  val uiState: StateFlow<CreditsUiState> = _uiState

  init {
    fetchCreditsUseCase(args.id)
      .onEach { result ->
        result.onSuccess { credits ->
          _uiState.update {
            it.copy(
              forms = mapOf(
                CreditsTab.Cast(credits.cast.size) to CreditsUiContent.Cast(credits.cast),
                CreditsTab.Crew(
                  credits.crewDepartments.sumOf { department -> department.uniqueCrewList.size },
                ) to CreditsUiContent.Crew(credits.crewDepartments),
              ),
              tabs = listOf(
                CreditsTab.Cast(credits.cast.size),
                CreditsTab.Crew(
                  credits.crewDepartments
                    .sumOf { department ->
                      department.crewList.distinctBy { crew -> crew.id }.size
                    },
                ),
              ),
            )
          }
        }
      }
      .launchIn(viewModelScope)
  }

  fun onTabSelected(tabIndex: Int) {
    _uiState.update {
      it.copy(selectedTabIndex = tabIndex)
    }
  }
}
