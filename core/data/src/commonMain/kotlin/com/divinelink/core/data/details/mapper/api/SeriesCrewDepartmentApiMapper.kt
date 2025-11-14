package com.divinelink.core.data.details.mapper.api

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.model.credits.JobsApi
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
  gender = Gender.from(gender),
  knownForDepartment = knownForDepartment,
  role = jobs.mapJobs(department),
)

private fun List<JobsApi>.mapJobs(department: String): List<PersonRole.Crew> = map { job ->
  PersonRole.Crew(
    job = job.job,
    creditId = job.creditId,
    totalEpisodes = job.episodeCount.toLong(),
    department = department,
  )
}
