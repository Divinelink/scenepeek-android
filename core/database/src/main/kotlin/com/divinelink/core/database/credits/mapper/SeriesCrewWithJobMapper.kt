package com.divinelink.core.database.credits.mapper

import com.divinelink.core.database.credits.crew.SeriesCrewWithJob
import com.divinelink.core.database.credits.model.CrewEntity

fun SeriesCrewWithJob.toEntity() = CrewEntity(
  id = crewId,
  creditId = jobCreditId!!,
  name = crewName,
  originalName = crewOriginalName,
  job = jobTitle!!,
  profilePath = crewProfilePath,
  department = crewDepartment,
  totalEpisodeCount = crewTotalEpisodeCount,
  aggregateCreditId = aggregateCreditId,
)
