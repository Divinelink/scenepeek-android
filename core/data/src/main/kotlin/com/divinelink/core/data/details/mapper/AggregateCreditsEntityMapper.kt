package com.divinelink.core.data.details.mapper

import com.divinelink.core.data.details.mapper.entity.map
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.model.credits.AggregateCredits

fun AggregateCreditsEntity.map() = AggregateCredits(
  cast = cast.map(),
  crewDepartments = crew.map(),
  id = id,
)
