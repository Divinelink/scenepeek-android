package com.divinelink.feature.discover.filters

import com.divinelink.core.model.Decade
import com.divinelink.core.model.Genre
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.discover.YearType
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

sealed interface SelectFilterAction {
  data object ClearGenres : SelectFilterAction
  data object ResetRatingFilters : SelectFilterAction
  data object Retry : SelectFilterAction
  data class SelectGenre(val genre: Genre) : SelectFilterAction
  data class SelectLanguage(val language: Language) : SelectFilterAction
  data class SelectCountry(val country: Country) : SelectFilterAction
  data class SearchFilters(val query: String?) : SelectFilterAction
  data class UpdateVoteRange(val voteAverage: DiscoverFilter.VoteAverage) : SelectFilterAction
  data class UpdateMinimumVotes(val votes: Int) : SelectFilterAction
  data class UpdateYearType(val type: YearType) : SelectFilterAction
  data class UpdateSingleYear(val year: Int) : SelectFilterAction
  data class UpdateStartYear(val startYear: Int) : SelectFilterAction
  data class UpdateEndYear(val endYear: Int) : SelectFilterAction
  data class OnSelectDecade(val decade: Decade) : SelectFilterAction
}
