package com.divinelink.core.model.sort

sealed interface SortContext {
  val availableOptions: List<SortBy>
  val defaultOption: SortBy

  fun find(value: String?): SortBy = availableOptions
    .find { it.value == value } ?: defaultOption

  data object TopRated : SortContext {
    override val availableOptions: List<SortBy> = listOf(
      SortBy.VoteAverage,
    )
    override val defaultOption: SortBy = SortBy.VoteAverage
  }

  data object DiscoverMovie : SortContext {
    override val availableOptions = listOf(
      SortBy.Popularity,
      SortBy.VoteAverage,
      SortBy.VoteCount,
      SortBy.ReleaseDate,
      SortBy.Revenue,
      SortBy.Title,
    )
    override val defaultOption = SortBy.Popularity
  }

  data object DiscoverShow : SortContext {
    override val availableOptions = listOf(
      SortBy.Popularity,
      SortBy.VoteAverage,
      SortBy.VoteCount,
      SortBy.FirstAirDate,
      SortBy.Name,
    )
    override val defaultOption = SortBy.Popularity
  }

  data object UpcomingMovie : SortContext {
    override val availableOptions = listOf(
      SortBy.Popularity,
      SortBy.ReleaseDate,
      SortBy.Title,
    )
    override val defaultOption = SortBy.Popularity
  }

  data object UpcomingShow : SortContext {
    override val availableOptions = listOf(
      SortBy.Popularity,
      SortBy.FirstAirDate,
      SortBy.Name,
    )
    override val defaultOption = SortBy.Popularity
  }

  data object UserData : SortContext {
    override val availableOptions = listOf(SortBy.CreatedAt)
    override val defaultOption = SortBy.CreatedAt
  }

  data object ListDetails : SortContext {
    override val availableOptions = listOf(
      SortBy.OriginalOrder,
      SortBy.ReleaseDate,
      SortBy.VoteAverage,
      SortBy.Title,
    )
    override val defaultOption = SortBy.OriginalOrder
  }
}
