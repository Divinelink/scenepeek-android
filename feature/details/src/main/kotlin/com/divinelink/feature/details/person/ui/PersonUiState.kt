package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.feature.details.person.ui.credits.PersonCreditsUiState

data class PersonUiState(
  val isLoading: Boolean = false,
  val isError: Boolean = false,
  val personDetails: PersonDetailsUiState = PersonDetailsUiState.Loading,
  val credits: PersonCreditsUiState = PersonCreditsUiState.Hidden,
  val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
)
