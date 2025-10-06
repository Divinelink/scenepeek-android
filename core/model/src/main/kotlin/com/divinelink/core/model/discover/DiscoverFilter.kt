package com.divinelink.core.model.discover

sealed interface DiscoverFilter {
  data class Genres(val filters: List<Int>) : DiscoverFilter
}
