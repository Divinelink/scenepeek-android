package com.divinelink.core.model.discover

import kotlinx.serialization.Serializable

@Serializable
enum class DiscoverType(val value: String) {
  Discover("discover"),
  Upcoming("upcoming"),
  Popular("popular"),
}
