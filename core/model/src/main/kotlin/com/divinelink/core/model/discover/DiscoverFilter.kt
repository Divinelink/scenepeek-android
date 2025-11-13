package com.divinelink.core.model.discover

sealed interface DiscoverFilter {
  data class Genres(val filters: List<Int>) : DiscoverFilter
  data class Language(val language: String) : DiscoverFilter
  data class Country(val countryCode: String) : DiscoverFilter
  data class VoteAverage(
    val greaterThan: Int,
    val lessThan: Int,
  ) : DiscoverFilter
}
