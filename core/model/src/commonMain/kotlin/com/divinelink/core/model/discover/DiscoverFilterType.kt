package com.divinelink.core.model.discover

enum class DiscoverFilterType {
  Genres,
  Year,
  Language,
  Country,
  Rating,
  Keywords,
  Companies,
  ;

  companion object {
    fun availability(entryPoint: DiscoverType): List<DiscoverFilterType> = when (entryPoint) {
      DiscoverType.Discover -> entries
      DiscoverType.Popular -> entries
      DiscoverType.TopRated -> entries
      DiscoverType.Upcoming -> entries.minus(setOf(Year, Rating))
    }
  }
}
