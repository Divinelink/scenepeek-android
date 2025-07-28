package com.divinelink.feature.lists.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.onError
import com.divinelink.core.domain.list.FetchListDetailsUseCase
import com.divinelink.core.domain.list.FetchListParameters
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListDetailsViewModel(
  private val fetchListDetailsUseCase: FetchListDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: ListDetailsRoute = ListDetailsRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    name = savedStateHandle.get<String>("name") ?: "",
    backdropPath = savedStateHandle.get<String>("backdropPath") ?: "",
    description = savedStateHandle.get<String>("description") ?: "",
    public = savedStateHandle.get<Boolean>("public") ?: false,
  )

  private val _uiState: MutableStateFlow<ListDetailsUiState> = MutableStateFlow(
    ListDetailsUiState.initial(
      id = route.id,
      name = route.name,
      backdropPath = route.backdropPath,
      description = route.description,
      public = route.public,
    ),
  )
  val uiState: StateFlow<ListDetailsUiState> = _uiState

  init {
    fetchListDetails(isRefreshing = false)
  }

  private fun fetchListDetails(isRefreshing: Boolean) {
    _uiState.update { uiState ->
      uiState.copy(
        loadingMore = uiState.details !is ListDetailsData.Initial && !isRefreshing,
      )
    }

    viewModelScope.launch {
      fetchListDetailsUseCase.invoke(
        FetchListParameters(
          listId = uiState.value.id,
          page = if (isRefreshing) 1 else uiState.value.page,
        ),
      )
        .distinctUntilChanged()
        .collect { result ->
          result
            .onSuccess { listDetails ->
              _uiState.update { uiState ->
                uiState.copy(
                  page = listDetails.page + 1,
                  details = if (isRefreshing) {
                    ListDetailsData.Data(
                      data = listDetails,
                    )
                  } else {
                    when (uiState.details) {
                      is ListDetailsData.Initial -> ListDetailsData.Data(
                        data = listDetails,
                      )
                      is ListDetailsData.Data -> ListDetailsData.Data(
                        data = listDetails.copy(
                          media = (uiState.details.data.media + listDetails.media)
                            .distinctBy { it.id },
                        ),
                      )
                    }
                  },
                  loadingMore = false,
                  refreshing = false,
                  error = null,
                )
              }
            }
            .onError<AppException.Offline> {
              if (uiState.value.details is ListDetailsData.Initial) {
                _uiState.update { uiState ->
                  uiState.copy(
                    error = BlankSlateState.Offline,
                    loadingMore = false,
                    refreshing = false,
                  )
                }
              }
            }
            .onFailure {
              if (uiState.value.details is ListDetailsData.Initial) {
                _uiState.update { uiState ->
                  uiState.copy(
                    error = BlankSlateState.Generic,
                    loadingMore = false,
                    refreshing = false,
                  )
                }
              } else {
                _uiState.update { uiState ->
                  uiState.copy(
                    loadingMore = false,
                    refreshing = false,
                  )
                }
              }
            }
        }
    }
  }

  fun onAction(action: ListDetailsAction) {
    when (action) {
      ListDetailsAction.LoadMore -> if (_uiState.value.canLoadMore()) {
        fetchListDetails(isRefreshing = false)
      }
      ListDetailsAction.Refresh -> refresh()
      is ListDetailsAction.OnItemClick -> {
        // Do nothing
      }
      is ListDetailsAction.SelectMedia -> _uiState.update { uiState ->
        if (uiState.selectedMediaIds.contains(action.mediaId)) {
          uiState.copy(
            selectedMediaIds = uiState.selectedMediaIds - action.mediaId,
          )
        } else {
          uiState.copy(
            multipleSelectMode = true,
            selectedMediaIds = uiState.selectedMediaIds + action.mediaId,
          )
        }
      }
      ListDetailsAction.OnDeselectAll -> _uiState.update {
        it.copy(
          selectedMediaIds = emptyList(),
        )
      }
      ListDetailsAction.OnSelectAll -> _uiState.update { uiState ->
        if (uiState.details is ListDetailsData.Data) {
          uiState.copy(selectedMediaIds = uiState.details.data.media.map { it.id })
        } else {
          uiState
        }
      }
      ListDetailsAction.OnDismissMultipleSelect -> _uiState.update { uiState ->
        uiState.copy(
          multipleSelectMode = false,
          selectedMediaIds = emptyList(),
        )
      }
    }
  }

  private fun refresh() {
    _uiState.update { uiState ->
      uiState.copy(
        refreshing = true,
      )
    }
    fetchListDetails(isRefreshing = true)
  }
}
