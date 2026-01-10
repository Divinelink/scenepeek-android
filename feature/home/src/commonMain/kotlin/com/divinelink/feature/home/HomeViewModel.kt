package com.divinelink.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class HomeViewModel(
  private val repository: MediaRepository,
  private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val searchStateManager: SearchStateManager,
  clock: Clock,
) : ViewModel() {

  private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
    HomeUiState.initial(
      sections = buildHomeSections(clock),
    ),
  )
  val uiState: StateFlow<HomeUiState> = _uiState

  init {
    fetchSections()
  }

  private fun fetchSections() {
    uiState.value.forms.keys.forEach { section ->
      fetchMediaSection(section)
    }
  }

  private fun fetchMediaSection(section: HomeSection) {
    when (section) {
      HomeSection.TrendingAll -> fetchTrending(section)
      is HomeSection.Popular,
      is HomeSection.Upcoming,
        -> fetchMediaLists(section)
    }
  }

  private fun handleResult(
    section: HomeSection,
    result: Result<PaginationData<MediaItem>>,
  ) {
    result.onSuccess { response ->
      _uiState.update { uiState ->
        val data = (uiState.forms[section] as? HomeForm.Data)?.pages ?: mapOf(
          1 to response.list,
        )

        uiState.copy(
          forms = uiState.forms.plus(
            section to HomeForm.Data(
              pages = data.plus(response.page to response.list),
              canLoadMore = response.canLoadMore(),
              hasError = false,
              isLoading = false,
            ),
          ),
          pages = uiState.pages.plus(
            section to response.page,
          ),
        )
      }
    }.onFailure { error ->
      if (uiState.value.forms.all { it.value is HomeForm.Initial }) {
        handleErrorForInitialForms(error)
      } else {
        if (getPage(section) == 1) {
          setWholeCurrentFormToError(section)
        } else {
          setErrorOnCurrentSectionWithData(section)
        }
      }
    }
  }

  private fun setWholeCurrentFormToError(section: HomeSection) {
    _uiState.update { uiState ->
      uiState.copy(
        forms = uiState.forms.plus(section to HomeForm.Error),
      )
    }
  }

  private fun setErrorOnCurrentSectionWithData(section: HomeSection) {
    _uiState.update { uiState ->
      val currentForm = uiState.forms[section]
      val pages = (currentForm as? HomeForm.Data)?.pages ?: emptyMap()

      uiState.copy(
        forms = uiState.forms.plus(
          section to HomeForm.Data(
            pages = pages,
            canLoadMore = false,
            isLoading = false,
            hasError = true,
          ),
        ),
      )
    }
  }

  private fun handleErrorForInitialForms(error: Throwable) {
    when (error) {
      is AppException.Offline -> _uiState.update { uiState ->
        uiState.copy(
          error = BlankSlateState.Offline,
        )
      }
      else -> _uiState.update { uiState ->
        uiState.copy(
          error = BlankSlateState.Generic,
        )
      }
    }
  }

  fun onMarkAsFavoriteClicked(movie: MediaItem) {
    if (movie !is MediaItem.Media) return

    viewModelScope.launch {
      markAsFavoriteUseCase(movie)
    }
  }

  /**
   * Checks whether to load more popularMovies movies,
   * or make a search query with incremented page.
   * If there are language selected, it will not load more movies.
   */
  fun onLoadNextPage(section: HomeSection) {
    val currentForm = uiState.value.forms[section]
    val form = currentForm as? HomeForm.Data

    if (form?.hasError == false && form.canLoadMore) {
      fetchMediaSection(section)
    }
  }

  fun onClearFiltersClicked() {
    _uiState.update { viewState ->
      viewState.copy(
        filters = HomeFilter.entries.map { it.filter },
        filteredResults = null,
      )
    }
  }

  private fun onRetry() {
    _uiState.update { uiState ->
      uiState.copy(error = null)
    }

    fetchSections()
  }

  private fun onRetrySection(section: HomeSection) {
    val newForm = when (val currentForm = uiState.value.forms[section]) {
      is HomeForm.Data -> currentForm.copy(
        hasError = false,
        canLoadMore = false,
        isLoading = true,
      )
      HomeForm.Error -> HomeForm.Initial
      HomeForm.Initial -> HomeForm.Initial
      null -> HomeForm.Initial
    }

    _uiState.update { uiState ->
      uiState.copy(
        forms = uiState.forms.plus(section to newForm),
      )
    }

    fetchMediaSection(section)
  }

  fun onAction(action: HomeAction) {
    when (action) {
      HomeAction.RetryAll -> onRetry()
      is HomeAction.LoadMore -> onLoadNextPage(action.section)
      is HomeAction.RetrySection -> onRetrySection(action.section)
    }
  }

  fun onNavigateToSearch() {
    searchStateManager.updateEntryPoint(SearchEntryPoint.HOME)
  }

  private fun getPage(page: HomeSection): Int = uiState.value.pages[page]?.plus(1) ?: 1

  private fun fetchMediaLists(section: HomeSection) {
    viewModelScope.launch {
      repository.fetchMediaLists(
        section = section,
        page = getPage(section),
      )
        .catch { emit(Result.failure(it)) }
        .collect { result ->
          handleResult(
            section = section,
            result = result,
          )
        }
    }
  }

  private fun fetchTrending(section: HomeSection) {
    viewModelScope.launch {
      repository.fetchTrending(
        page = getPage(section),
      )
        .catch { emit(Result.failure(it)) }
        .collect { result ->
          handleResult(
            section = section,
            result = result,
          )
        }
    }
  }
}
