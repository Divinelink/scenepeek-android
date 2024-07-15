package com.divinelink.core.database.credits.mapper

import com.divinelink.core.database.credits.cast.SeriesCastWithRole
import com.divinelink.core.database.credits.model.CastEntity

fun SeriesCastWithRole.toEntity() = CastEntity(
  id = castId,
  name = castName,
  originalName = castOriginalName,
  profilePath = castProfilePath,
  totalEpisodeCount = castTotalEpisodeCount,
  character = roleCharacter!!,
)
