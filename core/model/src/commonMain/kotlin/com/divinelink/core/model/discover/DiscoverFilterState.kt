package com.divinelink.core.model.discover

import com.divinelink.core.model.Genre
import com.divinelink.core.model.certification.Certification
import com.divinelink.core.model.details.Company
import com.divinelink.core.model.details.Keyword
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.media.MediaType

data class DiscoverFilterState(
  val genres: List<Genre>,
  val language: Language?,
  val country: Country?,
  val year: DiscoverFilter.Year?,
  val voteAverage: DiscoverFilter.VoteAverage?,
  val votes: Int?,
  val keywords: List<Keyword>,
  val companies: List<Company>,
  val contentRating: List<Certification>,
  val custom: DiscoverFilter.Custom?,
) {
  companion object {
    fun initial(
      mediaType: MediaType,
      entryPoint: DiscoverType,
      minDate: String,
    ) = DiscoverFilterState(
      genres = emptyList(),
      keywords = emptyList(),
      companies = emptyList(),
      year = null,
      language = null,
      country = null,
      voteAverage = null,
      votes = null,
      contentRating = emptyList(),
      custom = when (entryPoint) {
        DiscoverType.Upcoming -> DiscoverFilter.Custom.Upcoming(
          mediaType = mediaType,
          minDate = minDate,
        )
        DiscoverType.TopRated -> null
        DiscoverType.Popular -> null
        DiscoverType.Discover -> null
      },
    )

    val empty = DiscoverFilterState(
      genres = emptyList(),
      keywords = emptyList(),
      companies = emptyList(),
      contentRating = emptyList(),
      year = null,
      language = null,
      country = null,
      voteAverage = null,
      votes = null,
      custom = null,
    )
  }

  val hasSelectedFilters
    get() = genres.isNotEmpty() ||
      keywords.isNotEmpty() ||
      companies.isNotEmpty() ||
      contentRating.isNotEmpty() ||
      language != null ||
      country != null ||
      voteAverage != null ||
      votes != null ||
      year != null

  val firstSelectedFilterIndex: Int = when {
    genres.isNotEmpty() -> 0
    year != null -> 1
    language != null -> 2
    country != null -> 3
    voteAverage != null -> 4
    votes != null -> 5
    keywords.isNotEmpty() -> 6
    companies.isNotEmpty() -> 7
    contentRating.isNotEmpty() -> 8
    else -> 0
  }
}
