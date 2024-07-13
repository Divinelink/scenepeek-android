package com.divinelink.core.data.details.mapper

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.core.network.media.model.credits.SeriesCrewApi

/**
 * Create a list of [SeriesCrewDepartment] from a list of [SeriesCrewApi]
 * Each [SeriesCrewApi] belongs to a department, so we need to create all the departments
 * and add the crew to each department.
 */
fun List<SeriesCrewApi>.map(): List<SeriesCrewDepartment> {
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

fun SeriesCrewApi.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  role = PersonRole.Crew(
    job = jobs.firstOrNull()?.job,
    creditId = jobs.firstOrNull()?.creditId,
    totalEpisodes = totalEpisodeCount,
    department = department,
  ),
)
