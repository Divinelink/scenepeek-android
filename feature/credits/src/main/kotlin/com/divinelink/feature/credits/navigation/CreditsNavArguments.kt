package com.divinelink.feature.credits.navigation

import com.divinelink.core.model.media.MediaType

data class CreditsNavArguments(
  val id: Long,
  val mediaType: MediaType? = null,
)
