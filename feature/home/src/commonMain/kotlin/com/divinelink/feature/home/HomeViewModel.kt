package com.divinelink.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.home.toRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
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

  private fun fetchMediaSection(section: MediaListSection) {
    when (section) {
      MediaListSection.TrendingAll -> fetchTrending(section)
      is MediaListSection.Popular,
      is MediaListSection.Upcoming,
        -> fetchMediaLists(section)
      is MediaListSection.TopRated -> Unit
      MediaListSection.Favorites -> Unit
    }
  }

  private fun handleResult(
    section: MediaListSection,
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
          pages = if (response.page > (uiState.pages[section] ?: 0)) {
            uiState.pages.plus(section to response.page)
          } else {
            uiState.pages
          },
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

  private fun setWholeCurrentFormToError(section: MediaListSection) {
    _uiState.update { uiState ->
      uiState.copy(
        forms = uiState.forms.plus(section to HomeForm.Error),
      )
    }
  }

  private fun setErrorOnCurrentSectionWithData(section: MediaListSection) {
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

  private fun onLoadNextPage(section: MediaListSection) {
    val currentForm = uiState.value.forms[section]
    val form = currentForm as? HomeForm.Data

    if (form?.hasError == false && form.canLoadMore) {
      fetchMediaSection(section)
    }
  }

  private fun onRetry() {
    _uiState.update { uiState ->
      uiState.copy(error = null)
    }

    fetchSections()
  }

  private fun onRetrySection(section: MediaListSection) {
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

  private fun getPage(page: MediaListSection): Int = uiState.value.pages[page]?.plus(1) ?: 1

  private fun fetchMediaLists(section: MediaListSection) {
    val request = section.toRequest(
      mediaType = MediaType.UNKNOWN,
    ) ?: return

    viewModelScope.launch {
      repository.fetchMediaLists(
        request = request,
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

  private fun fetchTrending(section: MediaListSection) {
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
