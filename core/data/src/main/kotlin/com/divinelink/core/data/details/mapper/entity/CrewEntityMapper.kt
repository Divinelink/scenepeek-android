package com.divinelink.core.data.details.mapper.entity

import com.divinelink.core.database.credits.model.CrewEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person

fun List<CrewEntity>.map(): List<SeriesCrewDepartment> {
  val crewList = mutableListOf<SeriesCrewDepartment>()
  val departments = this.map { it.department }.distinct()

  departments.forEach { department ->
    val crew = this.filter { it.department == department }.map {
      it.map()
    }
    crewList.add(SeriesCrewDepartment(department, crew))
  }

  return crewList
}

fun CrewEntity.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  role = PersonRole.Crew(
    job = job,
    creditId = creditId,
    totalEpisodes = totalEpisodeCount,
    department = department,
  ),
)
