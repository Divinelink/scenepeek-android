package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.core.model.tab.Tab
import com.divinelink.feature.details.person.ui.filter.CreditFilter

data class PersonUiState(
  val selectedTabIndex: Int,
  val isLoading: Boolean = false,
  val isError: Boolean = false,
  val tabs: List<Tab>,
  val forms: Map<Int, PersonForm> = mapOf(
    PersonTab.About.order to PersonForm.About(PersonDetailsUiState.Loading),
    PersonTab.Movies.order to PersonForm.Movies(emptyMap()),
    PersonTab.TVShows.order to PersonForm.TvShows(emptyMap()),
  ),
  val filters: Map<Int, List<CreditFilter>> = mapOf(
    PersonTab.Movies.order to emptyList(),
    PersonTab.TVShows.order to emptyList(),
  ),
  val filteredCredits: Map<Int, GroupedPersonCredits> = mapOf(
    PersonTab.Movies.order to emptyMap(),
    PersonTab.TVShows.order to emptyMap(),
  ),
  val knownForCredits: List<PersonCredit>? = null,
  val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
) {
  val aboutForm = forms.getOrElse(PersonTab.About.order) { null } as? PersonForm.About
}
