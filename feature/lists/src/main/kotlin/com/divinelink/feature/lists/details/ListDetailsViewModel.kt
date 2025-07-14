package com.divinelink.feature.lists.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.domain.list.FetchListDetailsUseCase
import com.divinelink.core.domain.list.FetchListParameters
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ListDetailsViewModel(
  private val fetchListDetailsUseCase: FetchListDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: ListDetailsRoute = ListDetailsRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    name = savedStateHandle.get<String>("name") ?: "",
  )

  private val _uiState: MutableStateFlow<ListDetailsUiState> = MutableStateFlow(
    ListDetailsUiState.initial(
      id = route.id,
      name = route.name,
    ),
  )
  val uiState: StateFlow<ListDetailsUiState> = _uiState

  init {
    fetchListDetails(isRefreshing = false)
  }

  private fun fetchListDetails(isRefreshing: Boolean) {
    _uiState.update { uiState ->
      uiState.copy(
        loadingMore = uiState.details !is ListDetailsData.Initial,
      )
    }

    viewModelScope.launch {
      fetchListDetailsUseCase.invoke(
        FetchListParameters(
          listId = uiState.value.id,
          page = if (isRefreshing) 1 else uiState.value.page,
        ),
      ).collect { result ->
        result.fold(
          onSuccess = { listDetails ->
            _uiState.update { uiState ->
              uiState.copy(
                page = listDetails.page + 1,
                details = if (isRefreshing) {
                  ListDetailsData.Data(
                    data = listDetails,
                  )
                } else {
                  when (uiState.details) {
                    ListDetailsData.Initial -> ListDetailsData.Data(
                      data = listDetails,
                    )
                    is ListDetailsData.Data -> ListDetailsData.Data(
                      data = listDetails.copy(
                        media = uiState.details.data.media + listDetails.media,
                      ),
                    )
                  }
                },
                loadingMore = false,
                refreshing = false,
                error = null,
              )
            }
          },
          onFailure = { error ->
            if (uiState.value.details is ListDetailsData.Initial) {
              ErrorHandler.create(error) {
                on<UnknownHostException> {
                  _uiState.update { uiState ->
                    uiState.copy(
                      error = BlankSlateState.Offline,
                      loadingMore = false,
                      refreshing = false,
                    )
                  }
                }
                otherwise {
                  _uiState.update { uiState ->
                    uiState.copy(
                      error = BlankSlateState.Generic,
                      loadingMore = false,
                      refreshing = false,
                    )
                  }
                }
              }
            } else {
              // TODO Handle error when loading more
            }
          },
        )
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
