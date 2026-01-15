package com.divinelink.feature.media.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.home.toRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.feature.media.lists.ui.MediaListsForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MediaListsViewModel(
  private val repository: MediaRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val _uiState: MutableStateFlow<MediaListsUiState> = MutableStateFlow(
    MediaListsUiState.initial(
      section = Json.decodeFromString<MediaListSection>(savedStateHandle.get<String>("section")!!),
    ),
  )
  val uiState: StateFlow<MediaListsUiState> = _uiState

  fun onAction(action: MediaListsAction) {
    when (action) {
      MediaListsAction.LoadMore -> fetchMediaSection(uiState.value.selectedMediaType)
      MediaListsAction.Retry -> handleOnRetry()
      is MediaListsAction.OnSelectTab -> handleSelectTab(action)
    }
  }

  private fun fetchMediaSection(mediaType: MediaType) {
    when (uiState.value.section) {
      MediaListSection.TrendingAll -> fetchTrending()
      is MediaListSection.Popular -> fetchMediaLists(mediaType)
      is MediaListSection.TopRated -> fetchMediaLists(mediaType)
      is MediaListSection.Upcoming -> fetchMediaLists(mediaType)
      MediaListSection.Favorites -> fetchFavorites(mediaType)
    }
  }

  private fun handleOnRetry() {
    _uiState.update { uiState ->
      uiState.copy(
        forms = uiState.forms.plus(
          uiState.selectedMediaType to MediaListsForm.Initial,
        ),
      )
    }

    fetchMediaSection(uiState.value.selectedMediaType)
  }

  private fun handleSelectTab(action: MediaListsAction.OnSelectTab) {
    _uiState.update { uiState ->
      uiState.copy(
        selectedTabIndex = action.index,
      )
    }

    if (uiState.value.selectedForm is MediaListsForm.Initial) {
      fetchMediaSection(uiState.value.selectedMediaType)
    }
  }

  private fun fetchMediaLists(mediaType: MediaType) {
    val request = uiState.value.section.toRequest(mediaType) ?: return

    viewModelScope.launch {
      repository.fetchMediaLists(
        request = request,
        page = getPage(mediaType),
      )
        .catch { emit(Result.failure(it)) }
        .distinctUntilChanged()
        .collect { result ->
          handleResult(
            mediaType = mediaType,
            result = result,
          )
        }
    }
  }

  private fun fetchFavorites(mediaType: MediaType) {
    viewModelScope.launch {
      repository.fetchFavorites(mediaType)
        .catch { emit(Result.failure(it)) }
        .distinctUntilChanged()
        .collect { result ->
          handleResult(
            mediaType = mediaType,
            result = result,
          )
        }
    }
  }

  private fun fetchTrending() {
    viewModelScope.launch {
      repository.fetchTrending(
        page = getPage(uiState.value.selectedMediaType),
      )
        .catch { emit(Result.failure(it)) }
        .distinctUntilChanged()
        .collect { result ->
          handleResult(
            mediaType = MediaType.UNKNOWN,
            result = result,
          )
        }
    }
  }

  private fun handleResult(
    mediaType: MediaType,
    result: Result<PaginationData<MediaItem>>,
  ) {
    result.onSuccess { response ->
      _uiState.update { uiState ->
        val data = (uiState.forms[mediaType] as? MediaListsForm.Data)?.pages ?: mapOf(
          1 to response.list,
        )

        uiState.copy(
          forms = uiState.forms.plus(
            mediaType to MediaListsForm.Data(
              pages = data.plus(response.page to response.list),
              canLoadMore = response.canLoadMore(),
              hasError = false,
              isLoading = false,
            ),
          ),
          pages = if (response.page > (uiState.pages[mediaType] ?: 0)) {
            uiState.pages.plus(mediaType to response.page)
          } else {
            uiState.pages
          },
        )
      }
    }.onFailure { error ->
      _uiState.update { uiState ->
        when (uiState.forms[uiState.selectedMediaType]) {
          is MediaListsForm.Data -> uiState
          is MediaListsForm.Error,
          MediaListsForm.Initial,
            -> when (error) {
              is AppException.Offline -> uiState.copy(
                forms = uiState.forms.plus(mediaType to MediaListsForm.Error.Offline),
              )
              else -> uiState.copy(
                forms = uiState.forms.plus(mediaType to MediaListsForm.Error.Generic),
              )
            }
          null -> uiState
        }
      }
    }
  }

  private fun getPage(type: MediaType): Int = uiState.value.pages[type]?.plus(1) ?: 1
}
