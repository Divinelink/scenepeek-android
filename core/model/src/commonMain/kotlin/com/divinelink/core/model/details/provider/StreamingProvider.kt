package com.divinelink.core.model.details.provider

import kotlinx.serialization.Serializable

@Serializable
data class StreamingProvider(
  val logoPath: String,
  val providerId: Int,
  val providerName: String,
  val displayPriority: Int,
) {
  fun uniqueId(section: String) = "$providerId $providerName $displayPriority $section"
}
