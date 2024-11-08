package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.person.credits.PersonCredit

data class PersonUiState(
  val isLoading: Boolean = false,
  val isError: Boolean = false,
  val personDetails: PersonDetailsUiState = PersonDetailsUiState.Loading,
  val credits: List<PersonCredit>? = null,
  val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
)
