package com.divinelink.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaItem
import com.divinelink.feature.search.ui.MediaListsForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MediaListsViewModel(
  private val repository: MediaRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val _uiState: MutableStateFlow<MediaListsUiState> = MutableStateFlow(
    MediaListsUiState.initial(
      section = Json.decodeFromString<HomeSection>(savedStateHandle.get<String>("section")!!),
    ),
  )
  val uiState: StateFlow<MediaListsUiState> = _uiState

  init {
    fetchMediaSection()
  }

  fun onAction(action: MediaListsAction) {
    when (action) {
      MediaListsAction.LoadMore -> fetchMediaSection()
      MediaListsAction.Retry -> handleOnRetry()
    }
  }

  private fun fetchMediaSection() {
    when (val section = uiState.value.section) {
      HomeSection.TrendingAll -> fetchTrending()
      is HomeSection.Popular,
      is HomeSection.Upcoming,
        -> fetchMediaLists(section)
    }
  }

  private fun handleOnRetry() {
    _uiState.update { uiState ->
      uiState.copy(form = MediaListsForm.Initial)
    }

    fetchMediaSection()
  }

  private fun getPage(): Int = uiState.value.page.plus(1)

  private fun fetchMediaLists(section: HomeSection) {
    viewModelScope.launch {
      repository.fetchMediaLists(
        section = section,
        page = getPage(),
      )
        .catch { emit(Result.failure(it)) }
        .collect { result ->
          handleResult(
            result = result,
          )
        }
    }
  }

  private fun fetchTrending() {
    viewModelScope.launch {
      repository.fetchTrending(
        page = getPage(),
      )
        .catch { emit(Result.failure(it)) }
        .collect { result ->
          handleResult(
            result = result,
          )
        }
    }
  }

  private fun handleResult(
    result: Result<PaginationData<MediaItem>>,
  ) {
    result.onSuccess { response ->
      _uiState.update { uiState ->
        val data = (uiState.form as? MediaListsForm.Data)?.pages ?: mapOf(
          1 to response.list,
        )

        uiState.copy(
          form = MediaListsForm.Data(
            pages = data.plus(response.page to response.list),
            canLoadMore = response.canLoadMore(),
            hasError = false,
            isLoading = false,
          ),
          page = response.page,
        )
      }
    }.onFailure { error ->
      _uiState.update { uiState ->
        when (uiState.form) {
          is MediaListsForm.Data -> uiState
          is MediaListsForm.Error,
          MediaListsForm.Initial,
            -> when (error) {
            is AppException.Offline -> uiState.copy(form = MediaListsForm.Error.Offline)
            else -> uiState.copy(form = MediaListsForm.Error.Generic)
          }
        }
      }
    }
  }
}
