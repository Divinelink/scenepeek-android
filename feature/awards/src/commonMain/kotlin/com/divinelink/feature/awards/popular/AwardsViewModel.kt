package com.divinelink.feature.awards.popular

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AwardsViewModel : ViewModel() {

  private val _uiState: MutableStateFlow<AwardsUiState> =
    MutableStateFlow(AwardsUiState.initial)
  val uiState: StateFlow<AwardsUiState> = _uiState
}

// Main entry point - shows list of award ceremonies
//AwardsScreen.kt                    // or AwardCeremoniesScreen.kt

// Shows details about a specific ceremony (Golden Globes, Oscars, etc.)
//CeremonyDetailScreen.kt            // or AwardCeremonyDetailScreen.kt

// Shows all categories within a ceremony
//CeremonyCategoriesScreen.kt        // or AwardCategoriesScreen.kt

// Shows history of a specific category across all years
//CategoryHistoryScreen.kt           // or AwardCategoryDetailScreen.kt

// Future: Shows all awards for a specific year within a ceremony
//CeremonyYearScreen.kt              // or CeremonyByYearScreen.kt


//AwardsListScreen.kt                // Top level - all ceremonies
//AwardCeremonyScreen.kt             // Specific ceremony overview
//AwardCategoriesScreen.kt           // Categories within ceremony
//AwardCategoryScreen.kt             // Specific category with all years
