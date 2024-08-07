package com.divinelink.feature.details.person.ui.credits

import com.divinelink.core.model.person.credits.PersonCredit

sealed interface PersonCreditsUiState {
  data object Hidden : PersonCreditsUiState
  data class Visible(val knownFor: List<PersonCredit>) : PersonCreditsUiState
}
