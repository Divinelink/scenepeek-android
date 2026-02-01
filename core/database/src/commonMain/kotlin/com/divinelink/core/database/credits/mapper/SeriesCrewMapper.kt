package com.divinelink.core.database.credits.mapper

import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

fun SeriesCrew.toPerson(roles: List<SeriesCrewJob>) = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  knownForDepartment = knownForDepartment,
  gender = Gender.from(gender.toInt()),
  role = roles.map { role ->
    PersonRole.Crew(
      job = role.job,
      creditId = role.creditId,
      totalEpisodes = role.episodeCount,
      department = department,
    )
  },
)
