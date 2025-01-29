package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.LayoutStyle
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import com.divinelink.feature.details.person.ui.tab.PersonTab

data class PersonUiState(
  val selectedTabIndex: Int,
  val isLoading: Boolean = false,
  val isError: Boolean = false,
  val tabs: List<PersonTab>,
  val forms: Map<Int, PersonForm> = mapOf(
    PersonTab.ABOUT.order to PersonForm.About(PersonDetailsUiState.Loading),
    PersonTab.MOVIES.order to PersonForm.Movies(emptyMap()),
    PersonTab.TV_SHOWS.order to PersonForm.TvShows(emptyMap()),
  ),
  val filters: Map<Int, List<CreditFilter>> = mapOf(
    PersonTab.MOVIES.order to emptyList(),
    PersonTab.TV_SHOWS.order to emptyList(),
  ),
  val filteredCredits: Map<Int, GroupedPersonCredits> = mapOf(
    PersonTab.MOVIES.order to emptyMap(),
    PersonTab.TV_SHOWS.order to emptyMap(),
  ),
  val knownForCredits: List<PersonCredit>? = null,
  val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  val layoutStyle: LayoutStyle = LayoutStyle.LIST,
) {
  val aboutForm = forms.getOrElse(PersonTab.ABOUT.order) { null } as? PersonForm.About
}
