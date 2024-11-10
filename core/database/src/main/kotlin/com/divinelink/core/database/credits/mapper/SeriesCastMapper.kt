package com.divinelink.core.database.credits.mapper

import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.database.credits.model.CastEntity

fun SeriesCast.toEntity(roles: List<SeriesCastRole>): CastEntity = CastEntity(
  id = id,
  name = name,
  originalName = originalName,
  profilePath = profilePath,
  knownForDepartment = knownForDepartment,
  gender = gender,
  roles = roles,
)
