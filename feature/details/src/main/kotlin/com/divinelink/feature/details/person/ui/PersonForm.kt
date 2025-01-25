package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.feature.details.person.ui.filter.CreditFilter

sealed interface PersonForm {
  data class About(val personDetails: PersonDetailsUiState) : PersonForm
  data class Movies(val credits: GroupedPersonCredits) : PersonForm
  data class TvShows(val credits: GroupedPersonCredits) : PersonForm

  val availableFilters: List<CreditFilter>
    get() = when (this) {
      is Movies -> credits.keys.map { CreditFilter.Department(it, credits[it]?.size ?: 0) }
      is TvShows -> credits.keys.map { CreditFilter.Department(it, credits[it]?.size ?: 0) }
      is About -> emptyList()
    }
}
