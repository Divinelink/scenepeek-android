package com.divinelink.core.navigation.arguments

import com.divinelink.core.model.media.MediaType

data class CreditsNavArguments(
  val id: Long,
  val mediaType: MediaType?,
)
