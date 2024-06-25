package com.divinelink.core.model.jellyseerr

data class JellyseerrDetails(
  val address: String,
  val apiKey: String,
) {
  companion object {
    fun initial(): JellyseerrDetails = JellyseerrDetails(
      address = "",
      apiKey = "",
    )
  }
}
