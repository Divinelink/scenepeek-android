package com.divinelink.core.model.details

import kotlinx.serialization.Serializable

@Serializable
data class ExternalIds(
  val imdbId: String? = null,
  val wikidataId: String? = null,
)
