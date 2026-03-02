package com.divinelink.core.model.details.provider

import kotlinx.serialization.Serializable

@Serializable
data class StreamingProvider(
  val logoPath: String,
  val providerId: Int,
  val providerName: String,
  val displayPriority: Int,
) {
  val uniqueId
    get() = "$providerId $providerName $displayPriority"
}
