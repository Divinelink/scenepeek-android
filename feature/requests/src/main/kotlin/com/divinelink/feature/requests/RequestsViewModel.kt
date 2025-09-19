package com.divinelink.feature.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.domain.jellyseerr.DeleteMediaParameters
import com.divinelink.core.domain.jellyseerr.DeleteMediaUseCase
import com.divinelink.core.model.DataState
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.setLoading
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RequestsViewModel(
  private val repository: JellyseerrRepository,
  private val detailsRepository: DetailsRepository,
  private val deleteMediaUseCase: DeleteMediaUseCase,
  authRepository: AuthRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<RequestsUiState> = MutableStateFlow(
    RequestsUiState.initial,
  )
  val uiState: StateFlow<RequestsUiState> = _uiState

  private val requestsFlow = _uiState
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
    authRepository
      .profilePermissions
      .distinctUntilChanged()
      .onEach { permissions ->
        _uiState.update { it.copy(permissions = permissions) }
      }
      .launchIn(viewModelScope)

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
                loadingMore = false,
                canLoadMore = result.pageInfo.pages >= result.pageInfo.page,
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
      is RequestsAction.ApproveRequest -> {
      }
      is RequestsAction.DeclineRequest -> {
      }
      is RequestsAction.CancelRequest -> {
      }
      is RequestsAction.EditRequest -> {
      }
      is RequestsAction.DeleteRequest -> viewModelScope.launch {
        setLoading(
          requestId = action.id,
          loading = true,
        )

        repository.deleteRequest(action.id).fold(
          onSuccess = {
            removeItem(action.id)
          },
          onFailure = {
            setLoading(
              requestId = action.id,
              loading = true,
            )
          },
        )
      }
      is RequestsAction.RemoveFromServer -> {
        setLoading(
          requestId = action.requestId,
          loading = true,
        )

        viewModelScope.launch {
          deleteMediaUseCase.invoke(
            DeleteMediaParameters(
              mediaId = action.mediaId,
              deleteFile = true,
            ),
          ).fold(
            onSuccess = {
              removeItem(action.requestId)
            },
            onFailure = {
              setLoading(
                requestId = action.requestId,
                loading = false,
              )
            },
          )
        }
      }
      is RequestsAction.RetryRequest -> {
      }
      is RequestsAction.UpdateFilter -> updateFilter(action)
    }
  }

  private fun setLoading(
    requestId: Int,
    loading: Boolean,
  ) {
    _uiState.update {
      updateUiStateByRequestId(
        requestId = requestId,
        transform = { item ->
          item.copy(
            mediaState = item.mediaState.setLoading(loading),
          )
        },
      )
    }
  }

  private fun removeItem(requestId: Int) {
    _uiState.update {
      updateUiStateByRequestId(
        requestId = requestId,
        transform = { _ -> null },
      )
    }
  }

  private fun updateUiStateByRequestId(
    requestId: Int,
    transform: (RequestUiItem) -> RequestUiItem?,
  ): RequestsUiState {
    return _uiState.value.let { currentState ->
      val data = currentState.data as? DataState.Data ?: return currentState
      val pages = data.pages

      val pageKey =
        findPageForRequestId(pages = pages, requestId = requestId) ?: return currentState
      val currentList = pages[pageKey] ?: return currentState

      val updatedList = currentList.mapNotNull { uiItem ->
        if (uiItem.request.id == requestId) {
          transform(uiItem)
        } else {
          uiItem
        }
      }

      val newPages = pages.toMutableMap().apply {
        this[pageKey] = updatedList
      }.toMap()

      currentState.copy(data = DataState.Data(newPages))
    }
  }

  private fun updateFilter(action: RequestsAction.UpdateFilter) {
    _uiState.update { uiState ->
      if (action.filter == uiState.filter) {
        uiState
      } else {
        uiState.copy(
          filter = action.filter,
          page = 1,
          data = DataState.Initial,
        )
      }
    }
  }

  private fun incrementPage() {
    if (uiState.value.canLoadMore && !uiState.value.loadingMore) {
      _uiState.update { uiState ->
        uiState.copy(
          loadingMore = true,
          page = uiState.page + 1,
        )
      }
    }
  }

  private fun fetchMediaItem(item: RequestUiItem) {
    if (item.mediaState !is ItemState.Data) {
      viewModelScope.launch {
        detailsRepository.fetchMediaItem(item.request.media)
          .collect { result ->
            when (result) {
              is Resource.Error -> updateMediaItem(
                request = item,
                mediaItem = null,
              )
              is Resource.Loading -> updateMediaItem(
                request = item,
                mediaItem = result.data,
              )
              is Resource.Success -> updateMediaItem(
                request = item,
                mediaItem = result.data,
              )
            }
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
          uiItem.copy(
            mediaState = mediaItem?.let { item ->
              ItemState.Data(
                item = item,
                loading = false,
              )
            },
          )
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
