package com.divinelink.core.data.details.mapper

import com.divinelink.core.model.credits.SeriesCrew
import com.divinelink.core.model.credits.SeriesCrewDepartment
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

fun SeriesCrewApi.map() = SeriesCrew(
  id = id,
  creditId = this.jobs.firstOrNull()?.creditId,
  adult = adult,
  name = name,
  episodeCount = totalEpisodeCount,
  job = jobs.firstOrNull()?.job,
  profilePath = profilePath,
  department = department,
)
