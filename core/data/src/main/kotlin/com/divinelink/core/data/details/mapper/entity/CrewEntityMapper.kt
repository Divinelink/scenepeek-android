package com.divinelink.core.data.details.mapper.entity

import com.divinelink.core.database.credits.model.CrewEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

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
  knownForDepartment = knownForDepartment,
  gender = Gender.from(gender.toInt()),
  role = roles.map {
    PersonRole.Crew(
      job = it.job,
      creditId = it.creditId,
      totalEpisodes = it.episodeCount,
      department = department,
    )
  },
)
