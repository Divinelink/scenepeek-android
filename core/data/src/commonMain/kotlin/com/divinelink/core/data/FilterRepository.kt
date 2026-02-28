package com.divinelink.core.data

import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Keyword
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterRepository {
  private val filterStates = mutableMapOf<String, FilterState>()

  private data class FilterState(
    val selectedGenres: MutableStateFlow<Map<MediaType, List<Genre>>> = MutableStateFlow(
      mapOf(
        MediaType.MOVIE to emptyList(),
        MediaType.TV to emptyList(),
      ),
    ),
    val selectedLanguage: MutableStateFlow<Map<MediaType, Language?>> = MutableStateFlow(
      mapOf(
        MediaType.MOVIE to null,
        MediaType.TV to null,
      ),
    ),
    val voteAverage: MutableStateFlow<Map<MediaType, DiscoverFilter.VoteAverage?>> =
      MutableStateFlow(
        mapOf(
          MediaType.MOVIE to null,
          MediaType.TV to null,
        ),
      ),
    val selectedCountry: MutableStateFlow<Map<MediaType, Country?>> = MutableStateFlow(
      mapOf(
        MediaType.MOVIE to null,
        MediaType.TV to null,
      ),
    ),
    val minimumVotes: MutableStateFlow<Map<MediaType, Int?>> = MutableStateFlow(
      mapOf(
        MediaType.MOVIE to null,
        MediaType.TV to null,
      ),
    ),
    val year: MutableStateFlow<Map<MediaType, DiscoverFilter.Year?>> = MutableStateFlow(
      mapOf(
        MediaType.MOVIE to null,
        MediaType.TV to null,
      ),
    ),
    val keywords: MutableStateFlow<Map<MediaType, List<Keyword>>> = MutableStateFlow(
      mapOf(
        MediaType.MOVIE to emptyList(),
        MediaType.TV to emptyList(),
      ),
    ),
  )

  private fun getFilterState(uuid: String): FilterState = filterStates.getOrPut(uuid) {
    FilterState()
  }

  fun selectedGenres(uuid: String): StateFlow<Map<MediaType, List<Genre>>> =
    getFilterState(uuid).selectedGenres.asStateFlow()

  fun selectedLanguage(uuid: String): StateFlow<Map<MediaType, Language?>> =
    getFilterState(uuid).selectedLanguage.asStateFlow()

  fun voteAverage(uuid: String): StateFlow<Map<MediaType, DiscoverFilter.VoteAverage?>> =
    getFilterState(uuid).voteAverage.asStateFlow()

  fun selectedCountry(uuid: String): StateFlow<Map<MediaType, Country?>> =
    getFilterState(uuid).selectedCountry.asStateFlow()

  fun minimumVotes(uuid: String): StateFlow<Map<MediaType, Int?>> =
    getFilterState(uuid).minimumVotes.asStateFlow()

  fun year(uuid: String): StateFlow<Map<MediaType, DiscoverFilter.Year?>> =
    getFilterState(uuid).year.asStateFlow()

  fun keywords(uuid: String): StateFlow<Map<MediaType, List<Keyword>>> =
    getFilterState(uuid).keywords.asStateFlow()

  fun updateSelectedGenres(
    uuid: String,
    mediaType: MediaType,
    genres: List<Genre>,
  ) {
    getFilterState(uuid).selectedGenres.value += mediaType to genres
  }

  fun updateLanguage(
    uuid: String,
    mediaType: MediaType,
    language: Language?,
  ) {
    getFilterState(uuid).selectedLanguage.value += mediaType to language
  }

  fun updateCountry(
    uuid: String,
    mediaType: MediaType,
    country: Country?,
  ) {
    getFilterState(uuid).selectedCountry.value += mediaType to country
  }

  fun updateVoteAverage(
    uuid: String,
    mediaType: MediaType,
    voteAverage: DiscoverFilter.VoteAverage?,
  ) {
    getFilterState(uuid).voteAverage.value += mediaType to voteAverage
  }

  fun updateMinimumVotes(
    uuid: String,
    mediaType: MediaType,
    votes: Int,
  ) {
    getFilterState(uuid).minimumVotes.value += mediaType to votes
  }

  fun updateYear(
    uuid: String,
    mediaType: MediaType,
    year: DiscoverFilter.Year?,
  ) {
    getFilterState(uuid).year.value += mediaType to year
  }

  fun updateKeyword(
    uuid: String,
    mediaType: MediaType,
    keyword: Keyword,
  ) {
    val filterState = getFilterState(uuid)
    val currentKeywords = filterState.keywords.value[mediaType] ?: emptyList()

    val keywords = if (keyword in currentKeywords) {
      currentKeywords - keyword
    } else {
      currentKeywords + keyword
    }

    filterState.keywords.value += mediaType to keywords
  }

  fun clearKeywords(
    uuid: String,
    mediaType: MediaType,
  ) {
    getFilterState(uuid).keywords.value += mediaType to emptyList()
  }

  fun clearRatings(
    uuid: String,
    mediaType: MediaType,
  ) {
    getFilterState(uuid).voteAverage.value += mediaType to null
    getFilterState(uuid).minimumVotes.value += mediaType to null
  }

  fun clear(
    uuid: String,
    mediaType: MediaType,
  ) {
    with(getFilterState(uuid)) {
      selectedGenres.value += mediaType to emptyList()
      keywords.value += mediaType to emptyList()
      selectedLanguage.value += mediaType to null
      selectedCountry.value += mediaType to null
      voteAverage.value += mediaType to null
      minimumVotes.value += mediaType to null
      year.value += mediaType to null
    }
  }
}
