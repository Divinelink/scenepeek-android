package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.LayoutStyle
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.feature.details.person.ui.tab.PersonTab

data class PersonUiState(
  val selectedTabIndex: Int,
  val isLoading: Boolean = false,
  val isError: Boolean = false,
  val tabs: List<PersonTab>,
  val forms: Map<Int, PersonForm>,
  val personDetails: PersonDetailsUiState = PersonDetailsUiState.Loading,
  val knownForCredits: List<PersonCredit>? = null,
  val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  val layoutStyle: LayoutStyle = LayoutStyle.LIST,
)

sealed interface PersonForm {

  data class About(val personDetails: PersonDetailsUiState) : PersonForm

  data class Movies(val data: List<PersonCredit>) : PersonForm

  data class TvShows(val data: List<PersonCredit>) : PersonForm
}
