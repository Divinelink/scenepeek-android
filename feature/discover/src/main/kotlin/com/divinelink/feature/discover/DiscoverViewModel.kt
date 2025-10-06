package com.divinelink.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.GenreRepository
import com.divinelink.core.domain.DiscoverMediaUseCase
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.discover.DiscoverParameters
import com.divinelink.core.model.user.data.UserDataResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DiscoverViewModel(
  private val genreRepository: GenreRepository,
  private val discoverUseCase: DiscoverMediaUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<DiscoverUiState> = MutableStateFlow(
    DiscoverUiState.initial,
  )
  val uiState: StateFlow<DiscoverUiState> = _uiState

  init {
    genreRepository
      .selectedGenres
      .onEach { genres ->
        _uiState.update { uiState ->
          uiState.copy(
            genreFilters = uiState.genreFilters.plus(
              uiState.selectedTab.mediaType to genres.toList(),
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
    }
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
    if (reset) {
      _uiState.update { uiState ->
        uiState.copy(
          pages = uiState.pages + (uiState.selectedMedia to 1),
          loadingMap = uiState.loadingMap + (uiState.selectedMedia to true),
        )
      }
    }

    val genreFilters = uiState.value.selectedGenreFilters

    val discoverFilters = buildList {
      if (genreFilters.isNotEmpty()) {
        add(DiscoverFilter.Genres(genreFilters.map { it.id }))
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
          mediaType = uiState.value.selectedMedia,
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
            onFailure = {},
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

  override fun onCleared() {
    super.onCleared()
    genreRepository.clear()
  }
}
