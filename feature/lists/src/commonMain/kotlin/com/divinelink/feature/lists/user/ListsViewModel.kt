package com.divinelink.feature.lists.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.data
import com.divinelink.core.domain.list.FetchUserListsUseCase
import com.divinelink.core.domain.list.UserListsParameters
import com.divinelink.core.domain.list.mergeListItems
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListData
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.resources.Res
import com.divinelink.feature.lists.resources.feature_lists_login_description
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListsViewModel(private val fetchUserListsUseCase: FetchUserListsUseCase) : ViewModel() {

  private val _uiState: MutableStateFlow<ListsUiState> = MutableStateFlow(ListsUiState.initial)
  val uiState: StateFlow<ListsUiState> = _uiState

  init {
    fetchUserLists(isRefreshing = false)
  }

  fun onLoadMore() {
    val lists = uiState.value.lists

    if (lists is ListData.Data && lists.data.canLoadMore()) {
      fetchUserLists(isRefreshing = false)
    }
  }

  fun onRefresh() {
    _uiState.update { uiState ->
      uiState.copy(
        refreshing = true,
      )
    }
    fetchUserLists(isRefreshing = true)
  }

  @Suppress("LongMethod")
  private fun fetchUserLists(isRefreshing: Boolean) {
    _uiState.update { uiState ->
      uiState.copy(
        loadingMore = uiState.lists !is ListData.Initial && !isRefreshing,
      )
    }
    viewModelScope.launch {
      if (isRefreshing) {
        delay(250)
      }

      fetchUserListsUseCase(
        UserListsParameters(
          page = if (isRefreshing) 1 else uiState.value.page,
        ),
      )
        .collect { result ->
          result.fold(
            onSuccess = {
              _uiState.update { uiState ->
                uiState.copy(
                  loadingMore = false,
                  isLoading = false,
                  refreshing = false,
                  page = result.data.page + 1,
                  error = null,
                  lists = if (isRefreshing) {
                    ListData.Data(result.data)
                  } else {
                    when (uiState.lists) {
                      ListData.Initial -> ListData.Data(result.data)
                      is ListData.Data -> ListData.Data(
                        uiState.lists.data.copy(
                          page = result.data.page,
                          list = mergeListItems(
                            existingItems = uiState.lists.data.list,
                            newItems = result.data.list,
                            page = result.data.page - 1,
                          ),
                        ),
                      )
                    }
                  },
                )
              }
            },
            onFailure = {
              if (uiState.value.lists is ListData.Initial) {
                when (it) {
                  is SessionException.Unauthenticated -> setUnauthenticatedError()
                  is AppException.Offline -> _uiState.update { uiState ->
                    uiState.copy(
                      error = BlankSlateState.Offline,
                      isLoading = false,
                      loadingMore = false,
                      refreshing = false,
                    )
                  }
                  else -> _uiState.update { uiState ->
                    uiState.copy(
                      error = BlankSlateState.Generic,
                      isLoading = false,
                      loadingMore = false,
                      refreshing = false,
                    )
                  }
                }
              } else {
                _uiState.update { uiState ->
                  uiState.copy(
                    loadingMore = false,
                    isLoading = false,
                    refreshing = false,
                  )
                }
              }
            },
          )
        }
    }
  }

  private fun setUnauthenticatedError() {
    _uiState.update { uiState ->
      uiState.copy(
        error = BlankSlateState.Unauthenticated(
          UIText.ResourceText(Res.string.feature_lists_login_description),
        ),
        page = 1,
        lists = ListData.Initial,
        isLoading = false,
        loadingMore = false,
      )
    }
  }
}
