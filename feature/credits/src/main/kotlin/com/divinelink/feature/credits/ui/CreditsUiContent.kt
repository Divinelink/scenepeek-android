package com.divinelink.feature.credits.ui

import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person

sealed interface CreditsUiContent {
  data class Cast(val cast: List<Person>) : CreditsUiContent
  data class Crew(val crew: List<SeriesCrewDepartment>) : CreditsUiContent
}
