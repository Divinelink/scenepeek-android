package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.person.PersonDetails

sealed interface PersonDetailsUiState {
  data object Loading : PersonDetailsUiState
  data class Visible(val personDetails: PersonDetails) : PersonDetailsUiState
}
