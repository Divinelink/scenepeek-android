package com.divinelink.core.data.details.mapper

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi

fun AggregateCreditsApi.map() = AggregateCredits(
  cast = cast.map(),
  crewDepartments = crew.map(),
  id = id,
)
