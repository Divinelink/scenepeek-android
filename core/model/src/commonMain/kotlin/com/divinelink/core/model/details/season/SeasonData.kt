package com.divinelink.core.model.details.season

import com.divinelink.core.model.details.Person

sealed interface SeasonData {
  data class About(
    val overview: String?,
  ) : SeasonData

  data class Cast(
    val cast: List<Person>,
  ) : SeasonData
}
