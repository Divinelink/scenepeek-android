package com.divinelink.feature.awards.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.awards.AwardsRepository
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AwardsViewModel(
  private val awardsRepository: AwardsRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<AwardsUiState> = MutableStateFlow(
    AwardsUiState.initial,
  )
  val uiState: StateFlow<AwardsUiState> = _uiState

  init {
    fetchCeremonies()
  }

  fun onAction(action: AwardsAction) {
    when (action) {
      is AwardsAction.OnCeremonyClick -> Unit
      AwardsAction.OnRetry -> fetchCeremonies()
    }
  }

  private fun fetchCeremonies() {
    _uiState.update { state ->
      state.copy(loading = true)
    }

    viewModelScope.launch {
      awardsRepository
        .fetchAwardCeremonies()
        .fold(
          onSuccess = { result ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                ceremonies = result,
              )
            }
          },
          onFailure = {
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = BlankSlateState.Generic,
              )
            }
          },
        )
    }
  }
}

// Shows details about a specific ceremony (Golden Globes, Oscars, etc.)
// CeremonyDetailScreen.kt            // or AwardCeremonyDetailScreen.kt

// Shows all categories within a ceremony
// CeremonyCategoriesScreen.kt        // or AwardCategoriesScreen.kt

// Shows history of a specific category across all years
// CategoryHistoryScreen.kt           // or AwardCategoryDetailScreen.kt

// Future: Shows all awards for a specific year within a ceremony
// CeremonyYearScreen.kt              // or CeremonyByYearScreen.kt

// AwardsListScreen.kt                // Top level - all ceremonies
// AwardCeremonyScreen.kt             // Specific ceremony overview
// AwardCategoriesScreen.kt           // Categories within ceremony
// AwardCategoryScreen.kt             // Specific category with all years
