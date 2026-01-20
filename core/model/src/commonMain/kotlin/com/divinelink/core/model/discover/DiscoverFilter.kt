package com.divinelink.core.model.discover

sealed interface DiscoverFilter {
  data class Genres(val filters: List<Int>) : DiscoverFilter
  data class Language(val language: String) : DiscoverFilter
  data class Country(val countryCode: String) : DiscoverFilter
  data class VoteAverage(
    val greaterThan: Int,
    val lessThan: Int,
  ) : DiscoverFilter

  data class MinimumVotes(val votes: Int) : DiscoverFilter

  sealed interface Year : DiscoverFilter {
    data class Single(val year: Int) : Year

    sealed class Multiple(
      val startDateTime: String,
      val endDateTime: String,
    ) : Year

    data class Range(
      val startYear: Int,
      val endYear: Int,
    ) : Multiple(
      startDateTime = "$startYear-01-01",
      endDateTime = "$endYear-01-01",
    )

    data class Decade(
      val decade: com.divinelink.core.model.Decade,
    ) : Multiple(
      startDateTime = "${decade.startYear}-01-01",
      endDateTime = "${decade.endYear}-01-01",
    )
  }
}
