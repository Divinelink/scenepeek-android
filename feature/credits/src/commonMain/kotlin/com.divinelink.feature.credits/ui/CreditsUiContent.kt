package com.divinelink.feature.credits.ui

import com.divinelink.core.model.UIText
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.feature.credits.resources.Res
import com.divinelink.feature.credits.resources.feature_credits_cast_missing
import com.divinelink.feature.credits.resources.feature_credits_crew_missing

sealed interface CreditsUiContent {
  data class Cast(val cast: List<Person>) : CreditsUiContent {
    val castMissingText: UIText = UIText.ResourceText(Res.string.feature_credits_cast_missing)
  }

  data class Crew(val crew: List<SeriesCrewDepartment>) : CreditsUiContent {
    val crewMissingText: UIText = UIText.ResourceText(Res.string.feature_credits_crew_missing)
  }
}
