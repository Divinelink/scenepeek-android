package com.divinelink.feature.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.DataState
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RequestsViewModel(
  private val repository: JellyseerrRepository,
  private val detailsRepository: DetailsRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<RequestsUiState> = MutableStateFlow(
    RequestsUiState.initial,
  )
  val uiState: StateFlow<RequestsUiState> = _uiState

  private val requestsFlow = _uiState
    .map { it.copy(loading = true) }
    .distinctUntilChanged { old, new ->
      old.filter == new.filter && old.page == new.page
    }
    .flatMapMerge { state ->
      repository
        .getRequests(page = state.page, filter = state.filter)
        .catch { error ->
          Timber.e(error, "Failed to load requests")
        }
    }

  init {
    viewModelScope.launch {
      requestsFlow.collect { response ->
        response.fold(
          onFailure = {
            // Handle error
          },
          onSuccess = { result ->
            _uiState.update { uiState ->
              val requestUiItems = result.results.map { request ->
                RequestUiItem(
                  request = request,
                  mediaState = ItemState.Loading,
                )
              }

              uiState.copy(
                data = when (uiState.data) {
                  is DataState.Data -> DataState.Data(
                    uiState.data.pages + (result.pageInfo.page to requestUiItems),
                  )
                  DataState.Initial -> DataState.Data(mapOf(1 to requestUiItems))
                },
                loading = false,
                canLoadMore = result.pageInfo.pages != result.pageInfo.page,
                error = null,
              )
            }
          },
        )
      }
    }
  }

  fun onAction(action: RequestsAction) {
    when (action) {
      is RequestsAction.FetchMediaItem -> fetchMediaItem(action.request)
      RequestsAction.LoadMore -> incrementPage()
    }
  }

  private fun incrementPage() {
    if (uiState.value.canLoadMore) {
      _uiState.update { uiState ->
        uiState.copy(page = uiState.page + 1)
      }
    }
  }

  private fun fetchMediaItem(request: RequestUiItem) {
    viewModelScope.launch {
      detailsRepository.fetchMediaItem(request.request.media)
        .collect { result ->
          when (result) {
            is Resource.Error -> updateMediaItem(
              request = request,
              mediaItem = null,
            )
            is Resource.Loading -> updateMediaItem(
              request = request,
              mediaItem = result.data,
            )
            is Resource.Success -> updateMediaItem(
              request = request,
              mediaItem = result.data,
            )
          }
        }
    }
  }

  private fun updateMediaItem(
    request: RequestUiItem,
    mediaItem: MediaItem.Media?,
  ) {
    _uiState.update { uiState ->
      val currentPages = (uiState.data as? DataState.Data)?.pages ?: return
      val pageToUpdate = findPageForRequestId(
        pages = currentPages,
        requestId = request.request.id,
      ) ?: return
      val listToUpdate = currentPages[pageToUpdate] ?: return

      val updatedList = listToUpdate.map { uiItem ->
        if (uiItem.request.id == request.request.id) {
          uiItem.copy(mediaState = mediaItem?.let { item -> ItemState.Data(item) })
        } else {
          uiItem
        }
      }

      val newPages = currentPages + (pageToUpdate to updatedList)
      uiState.copy(data = DataState.Data(newPages))
    }
  }

  /**
   * Finds the page number that contains the request with the given ID.
   * Returns null if the request is not found in any loaded page.
   */
  private fun findPageForRequestId(
    pages: Map<Int, List<RequestUiItem>>,
    requestId: Int,
  ): Int? = pages.entries
    .firstOrNull { (_, itemList) -> itemList.any { it.request.id == requestId } }
    ?.key
}
