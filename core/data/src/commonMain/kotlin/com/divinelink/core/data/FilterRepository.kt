package com.divinelink.core.data

import com.divinelink.core.model.Genre
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterRepository {
  private val _selectedGenres = MutableStateFlow<Map<MediaType, List<Genre>>>(
    mapOf(
      MediaType.MOVIE to emptyList(),
      MediaType.TV to emptyList(),
    ),
  )
  val selectedGenres: StateFlow<Map<MediaType, List<Genre>>> = _selectedGenres.asStateFlow()

  private val _selectedLanguage = MutableStateFlow<Map<MediaType, Language?>>(
    mapOf(
      MediaType.MOVIE to null,
      MediaType.TV to null,
    ),
  )
  val selectedLanguage: StateFlow<Map<MediaType, Language?>> = _selectedLanguage.asStateFlow()

  private val _voteAverage = MutableStateFlow<Map<MediaType, DiscoverFilter.VoteAverage?>>(
    mapOf(
      MediaType.MOVIE to null,
      MediaType.TV to null,
    ),
  )
  val voteAverage: StateFlow<Map<MediaType, DiscoverFilter.VoteAverage?>> = _voteAverage
    .asStateFlow()

  private val _selectedCountry = MutableStateFlow<Map<MediaType, Country?>>(
    mapOf(
      MediaType.MOVIE to null,
      MediaType.TV to null,
    ),
  )
  val selectedCountry: StateFlow<Map<MediaType, Country?>> = _selectedCountry.asStateFlow()

  private val _minimumVotes = MutableStateFlow<Map<MediaType, Int?>>(
    mapOf(
      MediaType.MOVIE to null,
      MediaType.TV to null,
    ),
  )
  val minimumVotes: StateFlow<Map<MediaType, Int?>> = _minimumVotes.asStateFlow()

  private val _year = MutableStateFlow<Map<MediaType, DiscoverFilter.Year?>>(
    mapOf(
      MediaType.MOVIE to null,
      MediaType.TV to null,
    ),
  )
  val year: StateFlow<Map<MediaType, DiscoverFilter.Year?>> = _year.asStateFlow()

  fun updateSelectedGenres(
    mediaType: MediaType,
    genres: List<Genre>,
  ) {
    _selectedGenres.value += mediaType to genres
  }

  fun updateLanguage(
    mediaType: MediaType,
    language: Language?,
  ) {
    _selectedLanguage.value += mediaType to language
  }

  fun updateCountry(
    mediaType: MediaType,
    country: Country?,
  ) {
    _selectedCountry.value += mediaType to country
  }

  fun updateVoteAverage(
    mediaType: MediaType,
    voteAverage: DiscoverFilter.VoteAverage?,
  ) {
    _voteAverage.value += mediaType to voteAverage
  }

  fun updateMinimumVotes(
    mediaType: MediaType,
    votes: Int,
  ) {
    _minimumVotes.value += mediaType to votes
  }

  fun updateYear(
    mediaType: MediaType,
    year: DiscoverFilter.Year?,
  ) {
    _year.value += mediaType to year
  }

  fun clearRatings(mediaType: MediaType) {
    _voteAverage.value += mediaType to null
    _minimumVotes.value += mediaType to null
  }

  fun clear(mediaType: MediaType) {
    _selectedGenres.value += mediaType to emptyList()
    _selectedLanguage.value += mediaType to null
    _selectedCountry.value += mediaType to null
    _voteAverage.value += mediaType to null
    _minimumVotes.value += mediaType to null
    _year.value += mediaType to null
  }
}
