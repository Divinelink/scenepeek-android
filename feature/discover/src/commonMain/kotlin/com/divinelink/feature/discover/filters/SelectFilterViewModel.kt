package com.divinelink.feature.discover.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.FilterRepository
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.Decade
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.discover.YearType
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.discover.FilterModal
import com.divinelink.feature.discover.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectFilterViewModel(
  type: FilterModal,
  mediaType: MediaType,
  private val repository: MediaRepository,
  private val filterRepository: FilterRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<SelectFilterUiState> = MutableStateFlow(
    SelectFilterUiState.initial(
      filterModal = type,
      mediaType = mediaType,
    ),
  )
  val uiState: StateFlow<SelectFilterUiState> = _uiState

  init {
    when (type) {
      FilterModal.Country ->
        filterRepository
          .selectedCountry
          .map { it[_uiState.value.mediaType] }
          .distinctUntilChanged()
          .onEach { country ->
            _uiState.update {
              it.copy(
                filterType = (it.filterType as FilterType.Searchable.Countries).copy(
                  selectedOptions = country?.let { listOf(country) } ?: emptyList(),
                  query = null,
                ),
              )
            }
          }
          .launchIn(viewModelScope)

      FilterModal.Genre -> {
        fetchGenres(mediaType)
        filterRepository
          .selectedGenres
          .map { it[_uiState.value.mediaType] ?: emptyList() }
          .distinctUntilChanged()
          .onEach { genres ->
            _uiState.update {
              it.copy(
                filterType = (it.filterType as FilterType.Searchable.Genres).copy(
                  selectedOptions = genres,
                ),
              )
            }
          }
          .launchIn(viewModelScope)
      }
      FilterModal.Language ->
        filterRepository
          .selectedLanguage
          .map { it[_uiState.value.mediaType] }
          .distinctUntilChanged()
          .onEach { language ->
            _uiState.update {
              it.copy(
                filterType = (it.filterType as FilterType.Searchable.Languages).copy(
                  selectedOptions = language?.let { listOf(language) } ?: emptyList(),
                  query = null,
                ),
              )
            }
          }
          .launchIn(viewModelScope)

      FilterModal.VoteAverage -> {
        filterRepository
          .voteAverage
          .map { it[_uiState.value.mediaType] }

          .onEach { voteAverage ->
            _uiState.update {
              it.copy(
                filterType = (it.filterType as FilterType.VoteAverage).copy(
                  greaterThan = voteAverage?.greaterThan ?: 0,
                  lessThan = voteAverage?.lessThan ?: 10,
                ),
              )
            }
          }
          .launchIn(viewModelScope)

        filterRepository
          .minimumVotes
          .map { it[uiState.value.mediaType] }
          .onEach { votes ->
            _uiState.update {
              it.copy(
                filterType = (it.filterType as FilterType.VoteAverage).copy(
                  minimumVotes = votes ?: 10,
                ),
              )
            }
          }
          .launchIn(viewModelScope)
      }
      FilterModal.Year ->
        filterRepository
          .year
          .map { it[_uiState.value.mediaType] }
          .distinctUntilChanged()
          .onEach { filter ->
            _uiState.update { uiState ->
              when (filter) {
                is DiscoverFilter.Year.Range -> uiState.copy(
                  filterType = FilterType.Year.Range(
                    startYear = filter.startYear,
                    endYear = filter.endYear,
                  ),
                )
                is DiscoverFilter.Year.Decade -> uiState.copy(
                  filterType = FilterType.Year.Decade(decade = filter.decade),
                )
                is DiscoverFilter.Year.Single -> uiState.copy(
                  filterType = FilterType.Year.Single(year = filter.year),
                )
                null -> uiState.copy(filterType = FilterType.Year.Any)
              }
            }
          }
          .launchIn(viewModelScope)
    }
  }

  private fun fetchGenres(mediaType: MediaType) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          loading = true,
          error = null,
        )
      }

      repository.fetchGenres(mediaType).distinctUntilChanged().onEach { result ->
        when (result) {
          is Resource.Error -> _uiState.update {
            val blankSlate = when (result.error) {
              is AppException.Offline -> BlankSlateState.Offline
              else -> BlankSlateState.Generic
            }

            it.copy(
              error = blankSlate,
              loading = false,
            )
          }
          is Resource.Loading -> _uiState.update { uiState ->
            uiState.copy(
              loading = false,
              filterType = FilterType.Searchable.Genres(
                options = result.data ?: emptyList(),
                selectedOptions = emptyList(),
                query = null,
              ),
              error = null,
            )
          }
          is Resource.Success -> _uiState.update { uiState ->
            uiState.copy(
              loading = false,
              filterType = FilterType.Searchable.Genres(
                options = result.data,
                selectedOptions = emptyList(),
                query = null,
              ),
              error = null,
            )
          }
        }
      }.launchIn(viewModelScope)
    }
  }

  fun onAction(action: SelectFilterAction) {
    when (action) {
      SelectFilterAction.ClearGenres -> handleClearGenres()
      SelectFilterAction.ResetRatingFilters -> handleResetRatings()
      SelectFilterAction.Retry -> handleRetry()
      is SelectFilterAction.SelectGenre -> handleSelectGenre(action)
      is SelectFilterAction.SelectLanguage -> handleSelectLanguage(action)
      is SelectFilterAction.SelectCountry -> handleSelectCountry(action)
      is SelectFilterAction.UpdateVoteRange -> handleUpdateVoteRange(action)
      is SelectFilterAction.SearchFilters -> handleSearchFilters(action)
      is SelectFilterAction.UpdateMinimumVotes -> handleUpdateMinimumVotes(action)
      is SelectFilterAction.UpdateYearType -> handleUpdateYearType(action)
      is SelectFilterAction.UpdateSingleYear -> handleUpdateSingleYear(action.year)
      is SelectFilterAction.UpdateStartYear -> handleUpdateStartYear(action.startYear)
      is SelectFilterAction.UpdateEndYear -> handleUpdateEndYear(action.endYear)
      is SelectFilterAction.OnSelectDecade -> handleSelectDecade(action.decade)
    }
  }

  private fun handleSearchFilters(action: SelectFilterAction.SearchFilters) {
    _uiState.update { uiState ->
      uiState.copy(
        filterType = when (uiState.filterType) {
          is FilterType.Searchable.Countries -> uiState.filterType.copy(query = action.query)
          is FilterType.Searchable.Genres -> uiState.filterType.copy(query = action.query)
          is FilterType.Searchable.Languages -> uiState.filterType.copy(query = action.query)
          is FilterType.VoteAverage -> uiState.filterType
          is FilterType.Year -> uiState.filterType
        },
      )
    }
  }

  private fun handleClearGenres() {
    filterRepository.updateSelectedGenres(
      mediaType = _uiState.value.mediaType,
      genres = emptyList(),
    )
  }

  private fun handleResetRatings() {
    filterRepository.clearRatings(mediaType = _uiState.value.mediaType)
  }

  private fun handleRetry() {
    when (uiState.value.filterModal) {
      FilterModal.Genre -> fetchGenres(uiState.value.mediaType)
      FilterModal.Country -> Unit
      FilterModal.Language -> Unit
      FilterModal.VoteAverage -> Unit
      FilterModal.Year -> Unit
    }
  }

  private fun handleSelectGenre(action: SelectFilterAction.SelectGenre) {
    val selectedGenres = (_uiState.value.filterType as FilterType.Searchable.Genres).selectedOptions

    val genres = if (action.genre in selectedGenres) {
      selectedGenres - action.genre
    } else {
      selectedGenres + action.genre
    }

    filterRepository.updateSelectedGenres(
      mediaType = _uiState.value.mediaType,
      genres = genres,
    )
  }

  private fun handleSelectLanguage(action: SelectFilterAction.SelectLanguage) {
    val selectedLanguage = (_uiState.value.filterType as FilterType.Searchable.Languages)
      .selectedOptions

    val language = if (action.language in selectedLanguage) {
      null
    } else {
      action.language
    }
    filterRepository.updateLanguage(
      mediaType = _uiState.value.mediaType,
      language = language,
    )
  }

  private fun handleSelectCountry(action: SelectFilterAction.SelectCountry) {
    val selectedCountry = (_uiState.value.filterType as FilterType.Searchable.Countries)
      .selectedOptions

    val country = if (action.country in selectedCountry) {
      null
    } else {
      action.country
    }
    filterRepository.updateCountry(
      mediaType = _uiState.value.mediaType,
      country = country,
    )
  }

  private fun handleUpdateVoteRange(action: SelectFilterAction.UpdateVoteRange) {
    filterRepository.updateVoteAverage(
      mediaType = _uiState.value.mediaType,
      voteAverage = action.voteAverage,
    )
  }

  private fun handleUpdateMinimumVotes(action: SelectFilterAction.UpdateMinimumVotes) {
    filterRepository.updateMinimumVotes(
      mediaType = _uiState.value.mediaType,
      votes = action.votes,
    )
  }

  private fun handleUpdateYearType(action: SelectFilterAction.UpdateYearType) {
    val filter = when (action.type) {
      YearType.Any -> null
      YearType.Single -> DiscoverFilter.Year.Single(year = 2026)
      YearType.Range -> DiscoverFilter.Year.Range(
        startYear = 2026,
        endYear = 2026,
      )
      YearType.Decade -> DiscoverFilter.Year.Decade(decade = Decade.DECADE_2020)
    }

    filterRepository.updateYear(
      mediaType = _uiState.value.mediaType,
      year = filter,
    )
  }

  private fun handleUpdateSingleYear(year: Int) {
    filterRepository.updateYear(
      mediaType = _uiState.value.mediaType,
      year = DiscoverFilter.Year.Single(year = year),
    )
  }

  private fun handleSelectDecade(decade: Decade) {
    filterRepository.updateYear(
      mediaType = _uiState.value.mediaType,
      year = DiscoverFilter.Year.Decade(decade),
    )
  }

  private fun handleUpdateStartYear(startYear: Int) {
    val type = (uiState.value.filterType as? FilterType.Year.Range)?.copy(
      startYear = startYear,
    ) ?: return

    filterRepository.updateYear(
      mediaType = _uiState.value.mediaType,
      year = DiscoverFilter.Year.Range(
        startYear = startYear,
        endYear = type.endYear,
      ),
    )
  }

  private fun handleUpdateEndYear(endYear: Int) {
    val type = (uiState.value.filterType as? FilterType.Year.Range)?.copy(
      endYear = endYear,
    ) ?: return

    filterRepository.updateYear(
      mediaType = _uiState.value.mediaType,
      year = DiscoverFilter.Year.Range(
        startYear = type.startYear,
        endYear = endYear,
      ),
    )
  }
}
