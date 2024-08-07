package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.feature.details.person.ui.credits.PersonCreditsUiState

sealed interface PersonUiState {
  data class Success(
    val personDetails: PersonDetailsUiState = PersonDetailsUiState.Loading,
    val credits: PersonCreditsUiState = PersonCreditsUiState.Hidden,
    val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  ) : PersonUiState

  data object Loading : PersonUiState
  data object Error : PersonUiState
}

fun PersonUiState.updatePersonDetails(personDetails: PersonDetails): PersonUiState = when (this) {
  is PersonUiState.Success -> copy(
    personDetails = PersonDetailsUiState.Visible(personDetails),
  )
  else -> PersonUiState.Success(personDetails = PersonDetailsUiState.Visible(personDetails))
}

fun PersonUiState.updateCredits(knownFor: List<PersonCredit>): PersonUiState = when (this) {
  is PersonUiState.Success -> copy(
    credits = PersonCreditsUiState.Visible(
      knownFor = knownFor,
    ),
  )
  else -> PersonUiState.Success(
    credits = PersonCreditsUiState.Visible(
      knownFor = knownFor,
    ),
  )
}
