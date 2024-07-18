package com.divinelink.feature.credits.navigation

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.media.MediaType

data class CreditsNavArguments(
  val mediaType: MediaType? = null,
  val aggregateCredits: AggregateCredits? = null,
)
