package com.divinelink.feature.credits.ui

import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.UIText
import com.divinelink.feature.credits.R

sealed interface CreditsUiContent {
  data class Cast(val cast: List<Person>) : CreditsUiContent {
    val castMissingText: UIText = UIText.ResourceText(R.string.feature_credits_cast_missing)
  }

  data class Crew(val crew: List<SeriesCrewDepartment>) : CreditsUiContent {
    val crewMissingText: UIText = UIText.ResourceText(R.string.feature_credits_crew_missing)
  }
}
