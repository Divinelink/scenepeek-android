package com.divinelink.core.database.credits.mapper

import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.model.CrewEntity

fun SeriesCrew.toEntity(roles: List<SeriesCrewJob>) = CrewEntity(
  id = id,
  name = name,
  originalName = originalName,
  profilePath = profilePath,
  department = department,
  knownForDepartment = knownForDepartment,
  gender = gender,
  roles = roles,
)
