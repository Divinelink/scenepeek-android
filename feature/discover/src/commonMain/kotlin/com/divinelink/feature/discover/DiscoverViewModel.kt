package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.util.decodeFromString
import com.divinelink.core.data.FilterRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.domain.DiscoverMediaUseCase
import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Keyword
import com.divinelink.core.model.discover.DiscoverParameters
import com.divinelink.core.model.discover.MediaTypeFilters
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.navigation.route.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
  private val route: Navigation.DiscoverRoute,
  private val filterRepository: FilterRepository,
  private val discoverUseCase: DiscoverMediaUseCase,
  preferencesRepository: PreferencesRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> = MutableStateFlow(
    DiscoverUiState.initial(route),
  )
  val uiState: StateFlow<DiscoverUiState> = _uiState

  init {
    viewModelScope.launch {
      val mediaType = MediaType.from(route.mediaType)
      val genre = route.encodedGenre?.decodeFromString<Genre>()
      val keyword = route.encodedKeyword?.decodeFromString<Keyword>()

      genre?.let { genre ->
        filterRepository.updateSelectedGenres(
          mediaType = mediaType,
          uuid = route.entryPointUuid,
          genres = listOf(genre),
        )
      }

      keyword?.let { keyword ->
        filterRepository.updateKeyword(
          mediaType = mediaType,
          uuid = route.entryPointUuid,
          keyword = keyword,
        )
      }
    }

    filterRepository
      .selectedGenres(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] ?: emptyList() }
      .onEach { genres ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(genres = genres) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .selectedLanguage(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] }
      .onEach { language ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(language = language) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .selectedCountry(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] }
      .onEach { country ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(country = country) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .voteAverage(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] }
      .onEach { voteAverage ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(voteAverage = voteAverage) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .minimumVotes(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] }
      .onEach { votes ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(votes = votes) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .year(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] }
      .onEach { filter ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(year = filter) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    filterRepository
      .keywords(route.entryPointUuid)
      .map { it[uiState.value.selectedMedia] ?: emptyList() }
      .onEach { keywords ->
        _uiState.update { uiState ->
          uiState.copy(
            filters = uiState.filters.updateFilters(
              mediaType = uiState.selectedTab.mediaType,
              update = { it.copy(keywords = keywords) },
            ),
          )
        }
      }
      .launchIn(viewModelScope)

    preferencesRepository
      .uiPreferences
      .mapNotNull { uiPreferences ->
        uiPreferences.sortOption.mapNotNull { (key, value) ->
          when (key) {
            ViewableSection.DISCOVER_SHOWS -> MediaType.TV to value
            ViewableSection.DISCOVER_MOVIES -> MediaType.MOVIE to value
            else -> null
          }
        }.toMap()
      }
      .distinctUntilChanged()
      .onEach { sortMap ->
        _uiState.update { uiState -> uiState.copy(sortOption = sortMap) }
        handleDiscoverMedia(reset = true)
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: DiscoverAction) {
    when (action) {
      is DiscoverAction.OnSelectTab -> handleSelectTab(action)
      DiscoverAction.DiscoverMedia -> handleDiscoverMedia(reset = true)
      DiscoverAction.LoadMore -> handleLoadMore()
      DiscoverAction.ClearFilters -> handleClearFilters()
    }
  }

  private fun handleClearFilters() {
    filterRepository.clear(
      uuid = route.entryPointUuid,
      mediaType = _uiState.value.selectedMedia,
    )
    handleDiscoverMedia(reset = true)
  }

  private fun handleSelectTab(action: DiscoverAction.OnSelectTab) {
    _uiState.update {
      it.copy(selectedTabIndex = action.index)
    }
  }

  private fun handleLoadMore() {
    val selectedMedia = _uiState.value.selectedMedia
    val form = _uiState.value.forms[selectedMedia]
    val canFetchMore = _uiState.value.canFetchMore[selectedMedia] == true

    if (form is DiscoverForm.Data && canFetchMore) {
      handleDiscoverMedia(reset = false)
    }
  }

  private fun handleDiscoverMedia(reset: Boolean) {
    val mediaType = uiState.value.selectedMedia
    val currentFilters = uiState.value.currentFilters

    if (reset) {
      _uiState.update { uiState ->
        uiState.copy(
          pages = uiState.pages + (uiState.selectedMedia to 1),
          loadingMap = uiState.loadingMap + (uiState.selectedMedia to true),
        )
      }
    }

    if (currentFilters.hasSelectedFilters) {
      discoverUseCase.invoke(
        parameters = DiscoverParameters(
          page = uiState.value.pages[uiState.value.selectedMedia] ?: 1,
          sortOption = uiState.value.sortOption[uiState.value.selectedMedia]
            ?: SortOption.defaultDiscoverSortOption,
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
              )
            },
            onFailure = { error ->
              updateUiOnFailure(
                type = mediaType,
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
          forms = uiState.forms.plus(uiState.selectedMedia to DiscoverForm.Initial),
          loadingMap = uiState.loadingMap + (uiState.selectedMedia to false),
        )
      }
    }
  }

  private fun updateUiOnSuccess(
    reset: Boolean,
    response: UserDataResponse,
  ) {
    _uiState.update { uiState ->
      val data = (uiState.forms[response.type] as? DiscoverForm.Data)?.paginationData ?: mapOf(
        1 to response.data,
      )

      uiState.copy(
        forms = uiState.forms.plus(
          response.type to DiscoverForm.Data(
            mediaType = response.type,
            paginationData = if (reset) {
              mapOf(1 to response.data)
            } else {
              data.plus(response.page to response.data)
            },
            totalResults = response.totalResults,
          ),
        ),
        pages = uiState.pages + (response.type to response.page + 1),
        canFetchMore = uiState.canFetchMore + (response.type to response.canFetchMore),
        loadingMap = uiState.loadingMap + (uiState.selectedMedia to false),
      )
    }
  }

  private fun updateUiOnFailure(
    reset: Boolean,
    type: MediaType,
    error: Throwable,
  ) {
    _uiState.update { uiState ->
      uiState.copy(
        forms = if (uiState.forms[type] !is DiscoverForm.Data || reset) {
          uiState.forms.plus(
            type to when (error) {
              is AppException.Offline -> DiscoverForm.Error.Network
              else -> DiscoverForm.Error.Unknown
            },
          )
        } else {
          uiState.forms
        },
        loadingMap = uiState.loadingMap + (uiState.selectedMedia to false),
      )
    }
  }

  override fun onCleared() {
    super.onCleared()
    filterRepository.clear(uuid = route.entryPointUuid, mediaType = MediaType.TV)
    filterRepository.clear(uuid = route.entryPointUuid, mediaType = MediaType.MOVIE)
  }

  private fun Map<MediaType, MediaTypeFilters>.updateFilters(
    mediaType: MediaType,
    update: (MediaTypeFilters) -> MediaTypeFilters,
  ): Map<MediaType, MediaTypeFilters> {
    val current = this[mediaType] ?: MediaTypeFilters.initial
    return this + (mediaType to update(current))
  }
}
