package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.PersonDetails

sealed interface PersonUiState {
  data class Success(
    val personDetails: PersonDetails,
    val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  ) : PersonUiState

  data object Loading : PersonUiState
  data object Error : PersonUiState
}
