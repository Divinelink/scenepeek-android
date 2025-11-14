package com.divinelink.feature.credits.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.navigation.route.Navigation.CreditsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreditsViewModel(
  fetchCreditsUseCase: FetchCreditsUseCase,
  savedStateHandle: SavedStateHandle,
  private val spoilersObfuscationUseCase: SpoilersObfuscationUseCase,
) : ViewModel() {

  private val route: CreditsRoute = CreditsRoute(
    id = savedStateHandle.get<Long>("id")!!,
    mediaType = savedStateHandle["mediaType"],
  )

  private val _uiState: MutableStateFlow<CreditsUiState> = MutableStateFlow(
    CreditsUiState.initial(),
  )

  val uiState: StateFlow<CreditsUiState> = _uiState

  init {
    fetchCreditsUseCase(route.id)
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

    viewModelScope.launch {
      spoilersObfuscationUseCase(Unit).collect { obfuscateSpoilers ->
        _uiState.update {
          it.copy(obfuscateSpoilers = obfuscateSpoilers.data)
        }
      }
    }
  }

  fun onTabSelected(tabIndex: Int) {
    _uiState.update {
      it.copy(selectedTabIndex = tabIndex)
    }
  }

  fun onObfuscateSpoilers() {
    viewModelScope.launch {
      spoilersObfuscationUseCase.setSpoilersObfuscation(
        !uiState.value.obfuscateSpoilers,
      )
    }
  }
}
