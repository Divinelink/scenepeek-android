package com.divinelink.feature.details.person.ui

import androidx.compose.runtime.Immutable
import com.divinelink.core.model.LayoutStyle
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import com.divinelink.feature.details.person.ui.tab.PersonTab

@Immutable
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
  val knownForCredits: List<PersonCredit>? = null,
  val dropdownMenuItems: List<DetailsMenuOptions> = listOf(DetailsMenuOptions.SHARE),
  val layoutStyle: LayoutStyle = LayoutStyle.LIST,
) {
  val filteredCredits: Map<Int, GroupedPersonCredits> = forms.mapValues { (_, form) ->
    when (form) {
      is PersonForm.Movies -> applyFilters(
        credits = form.credits,
        filter = (filters[PersonTab.MOVIES.order] ?: emptyList()).firstOrNull(),
      )
      is PersonForm.TvShows -> applyFilters(
        credits = form.credits,
        filter = (filters[PersonTab.TV_SHOWS.order] ?: emptyList()).firstOrNull(),
      )
      is PersonForm.About -> emptyMap()
    }
  }

  val personDetails = forms[PersonTab.ABOUT.order]?.let { form ->
    when (form) {
      is PersonForm.About -> form.personDetails
      else -> null
    }
  }

  private fun applyFilters(
    credits: GroupedPersonCredits,
    filter: CreditFilter?,
  ): GroupedPersonCredits = credits
    .filterKeys { department ->
      when (filter) {
        is CreditFilter.Department -> department == filter.department
        CreditFilter.SortReleaseDate -> true
        null -> true
      }
    }
    .mapValues { (_, credits) -> credits }
}
