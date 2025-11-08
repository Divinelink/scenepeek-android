package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.FilterRepository
import com.divinelink.core.domain.DiscoverMediaUseCase
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.discover.DiscoverParameters
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DiscoverViewModel(
  private val filterRepository: FilterRepository,
  private val discoverUseCase: DiscoverMediaUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> = MutableStateFlow(
    DiscoverUiState.initial,
  )
  val uiState: StateFlow<DiscoverUiState> = _uiState

  init {
    filterRepository
      .selectedGenres
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
      .selectedLanguage
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
      .selectedCountry
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
    filterRepository.clear(mediaType = _uiState.value.selectedMedia)
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

    if (reset) {
      _uiState.update { uiState ->
        uiState.copy(
          pages = uiState.pages + (uiState.selectedMedia to 1),
          loadingMap = uiState.loadingMap + (uiState.selectedMedia to true),
        )
      }
    }
    val discoverFilters = with(_uiState.value.currentFilters) {
      buildList {
        if (genres.isNotEmpty()) {
          add(DiscoverFilter.Genres(genres.map { it.id }))
        }
        language?.let { filter -> add(DiscoverFilter.Language(filter.code)) }
        country?.let { add(DiscoverFilter.Country(it.code)) }
      }
    }

    if (discoverFilters.isEmpty()) {
      _uiState.update { uiState ->
        uiState.copy(
          forms = uiState.forms.plus(uiState.selectedMedia to DiscoverForm.Initial),
          loadingMap = uiState.loadingMap + (uiState.selectedMedia to false),
        )
      }
    } else {
      discoverUseCase.invoke(
        parameters = DiscoverParameters(
          page = uiState.value.pages[uiState.value.selectedMedia] ?: 1,
          mediaType = mediaType,
          filters = discoverFilters,
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
    filterRepository.clear(mediaType = _uiState.value.selectedMedia)
  }

  private fun Map<MediaType, MediaTypeFilters>.updateFilters(
    mediaType: MediaType,
    update: (MediaTypeFilters) -> MediaTypeFilters,
  ): Map<MediaType, MediaTypeFilters> {
    val current = this[mediaType] ?: MediaTypeFilters.initial
    return this + (mediaType to update(current))
  }
}
