package com.divinelink.core.model.credits

import com.divinelink.core.model.details.Person
import kotlinx.serialization.Serializable

@Serializable
data class SeriesCrewDepartment(
  val department: String,
  val crewList: List<Person>,
) {
  val uniqueCrewList: List<Person>
    get() = crewList.distinctBy { it.id }
}
