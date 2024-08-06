package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCredit

sealed interface PersonUiState {
  data class Success(
    val personDetails: PersonDetails,
    val credits: PersonCreditsUiState = PersonCreditsUiState.Hidden,
    val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  ) : PersonUiState

  data object Loading : PersonUiState
  data object Error : PersonUiState
}

sealed interface PersonCreditsUiState {
  data object Hidden : PersonCreditsUiState
  data class Visible(val knownFor: List<PersonCredit>) : PersonCreditsUiState
}
