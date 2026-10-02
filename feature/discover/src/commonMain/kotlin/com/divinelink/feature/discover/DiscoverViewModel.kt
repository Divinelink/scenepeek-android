package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.commons.util.decodeFromString
import com.divinelink.core.data.FilterRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.DiscoverMediaUseCase
import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Company
import com.divinelink.core.model.details.Keyword
import com.divinelink.core.model.discover.DiscoverParameters
import com.divinelink.core.model.discover.MediaTypeFilters
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.navigation.route.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
  mediaType: MediaType,
  private val route: Navigation.Discover,
  private val filterRepository: FilterRepository,
  private val discoverUseCase: DiscoverMediaUseCase,
  preferencesRepository: PreferencesRepository,
  clock: ClockProvider,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> = MutableStateFlow(
    DiscoverUiState.initial(
      route = route,
      mediaType = mediaType,
      minDate = clock.nowDT.date.toString(),
    ),
  )
  val uiState: StateFlow<DiscoverUiState> = _uiState

  init {
    val uuid = route.entryPointUuid

    viewModelScope.launch {
      val genre = (route as? Navigation.Discover.Default)
        ?.encodedGenre
        ?.decodeFromString<Genre>()
      val keyword = (route as? Navigation.Discover.Default)
        ?.encodedKeyword
        ?.decodeFromString<Keyword>()
      val company = (route as? Navigation.Discover.Default)
        ?.encodedCompany
        ?.decodeFromString<Company>()

      genre?.let { genre ->
        filterRepository.updateSelectedGenres(
          mediaType = mediaType,
          uuid = uuid,
          genres = listOf(genre),
        )
      }

      keyword?.let { keyword ->
        filterRepository.updateKeyword(
          mediaType = mediaType,
          uuid = uuid,
          keyword = keyword,
        )
      }

      company?.let { company ->
        filterRepository.updateCompany(
          mediaType = mediaType,
          uuid = uuid,
          company = company,
        )
      }
    }

    filterRepository
      .selectedGenres(uuid)
      .map { it[uiState.value.mediaType] ?: emptyList() }
      .onEach { genres ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(genres = genres) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .selectedLanguage(uuid)
      .map { it[_uiState.value.mediaType] }
      .onEach { language ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(language = language) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .selectedCountry(uuid)
      .map { it[_uiState.value.mediaType] }
      .onEach { country ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(country = country) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .voteAverage(uuid)
      .map { it[_uiState.value.mediaType] }
      .onEach { voteAverage ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(voteAverage = voteAverage) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .minimumVotes(uuid)
      .map { it[_uiState.value.mediaType] }
      .onEach { votes ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(votes = votes) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .year(uuid)
      .map { it[_uiState.value.mediaType] }
      .onEach { filter ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(year = filter) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .keywords(uuid)
      .map { it[_uiState.value.mediaType] ?: emptyList() }
      .onEach { keywords ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(keywords = keywords) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .companies(uuid)
      .map { it[_uiState.value.mediaType] ?: emptyList() }
      .onEach { companies ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              update = { it.copy(companies = companies) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    preferencesRepository
      .uiPreferences
      .map { uiPreferences ->
        val entryPoint = _uiState.value.entryPoint
        val mediaType = _uiState.value.mediaType

        if (mediaType == MediaType.MOVIE) {
          val movieSection = SortableSection.Discover.Movies(entryPoint)
          uiPreferences.sortOption[movieSection] ?: SortOption.default(movieSection.sortContext)
        } else {
          val tvSection = SortableSection.Discover.Shows(entryPoint)
          uiPreferences.sortOption[tvSection] ?: SortOption.default(tvSection.sortContext)
        }
      }
      .distinctUntilChanged()
      .onEach { sortMap ->
        _uiState.update { uiState -> uiState.copy(sortOption = sortMap) }
        handleDiscoverMedia(reset = true, shouldFetch = true)
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: DiscoverAction) {
    when (action) {
      DiscoverAction.DiscoverMedia -> handleDiscoverMedia(reset = true, shouldFetch = false)
      DiscoverAction.LoadMore -> handleLoadMore()
      DiscoverAction.ClearFilters -> handleClearFilters()
    }
  }

  private fun handleClearFilters() {
    filterRepository.clear(
      uuid = _uiState.value.uuid,
      mediaType = _uiState.value.mediaType,
    )
    handleDiscoverMedia(reset = true, shouldFetch = false)
  }

  private fun handleLoadMore() {
    val form = _uiState.value.form
    val canFetchMore = _uiState.value.canFetchMore

    if (form is DiscoverForm.Data && canFetchMore) {
      handleDiscoverMedia(reset = false, shouldFetch = canFetchMore)
    }
  }

  private fun handleDiscoverMedia(
    reset: Boolean,
    shouldFetch: Boolean,
  ) {
    val mediaType = _uiState.value.mediaType
    val currentFilters = uiState.value.currentFilters
    val lastFilters = uiState.value.currentLastFilters

    if (currentFilters != lastFilters || shouldFetch) {
      if (reset) {
        _uiState.update { uiState ->
          uiState.copy(
            page = 1,
            loading = true,
          )
        }
      }

      val sortOption = uiState.value.sortOption ?: return

      if (currentFilters.hasSelectedFilters || currentFilters.custom != null) {
        discoverUseCase.invoke(
          parameters = DiscoverParameters(
            page = uiState.value.page,
            sortOption = sortOption,
            mediaType = mediaType,
            filters = currentFilters,
          ),
        )
          .distinctUntilChanged()
          .onEach { result ->
            result.fold(
              onSuccess = { response ->
                updateUiOnSuccess(
                  reset = reset,
                  response = response,
                  filters = currentFilters,
                )
              },
              onFailure = { error ->
                updateUiOnFailure(
                  error = error,
                  reset = reset,
                )
              },
            )
          }
          .launchIn(viewModelScope)
      } else {
        _uiState.update { uiState ->
          uiState.copy(
            form = DiscoverForm.Initial,
            loading = false,
          )
        }
      }
    }
  }

  private fun updateUiOnSuccess(
    reset: Boolean,
    response: UserDataResponse,
    filters: MediaTypeFilters,
  ) {
    _uiState.update { uiState ->
      val data = (uiState.form as? DiscoverForm.Data)?.paginationData ?: mapOf(
        1 to response.data,
      )

      uiState.copy(
        form = DiscoverForm.Data(
          mediaType = response.type,
          paginationData = if (reset) {
            mapOf(1 to response.data)
          } else {
            data.plus(response.page to response.data)
          },
          totalResults = response.totalResults,
        ),
        page = response.page + 1,
        canFetchMore = response.canFetchMore,
        loading = false,
        lastFilters = uiState.filters.updateFilters(
          update = { filters },
        ),
      )
    }
  }

  private fun updateUiOnFailure(
    reset: Boolean,
    error: Throwable,
  ) {
    _uiState.update { uiState ->
      uiState.copy(
        form = if (uiState.form !is DiscoverForm.Data || reset) {
          when (error) {
            is AppException.Offline -> DiscoverForm.Error.Network
            else -> DiscoverForm.Error.Unknown
          }
        } else {
          uiState.form
        },
        loading = false,
      )
    }
  }

  override fun onCleared() {
    super.onCleared()
    filterRepository.clear(uuid = uiState.value.uuid, mediaType = MediaType.TV)
    filterRepository.clear(uuid = uiState.value.uuid, mediaType = MediaType.MOVIE)
  }

  private fun MediaTypeFilters.updateFilters(
    update: (MediaTypeFilters) -> MediaTypeFilters,
  ): MediaTypeFilters {
    val current = this

    return update(current)
  }
}
