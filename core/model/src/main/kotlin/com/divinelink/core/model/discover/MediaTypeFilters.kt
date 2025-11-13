package com.divinelink.core.model.discover

import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

data class MediaTypeFilters(
  val genres: List<Genre>,
  val language: Language?,
  val country: Country?,
  val voteAverage: DiscoverFilter.VoteAverage?,
  val votes: Int?,
) {
  companion object {
    val initial = MediaTypeFilters(
      genres = emptyList(),
      language = null,
      country = null,
      voteAverage = null,
      votes = null,
    )
  }

  val hasSelectedFilters
    get() = genres.isNotEmpty() ||
      language != null ||
      country != null ||
      voteAverage != null ||
      votes != null
}
