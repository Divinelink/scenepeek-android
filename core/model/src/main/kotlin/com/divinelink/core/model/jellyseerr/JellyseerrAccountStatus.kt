package com.divinelink.core.model.jellyseerr

data class JellyseerrAccountStatus(
  val username: String,
  val address: String,
  val signInMethod: JellyseerrLoginMethod,
)
