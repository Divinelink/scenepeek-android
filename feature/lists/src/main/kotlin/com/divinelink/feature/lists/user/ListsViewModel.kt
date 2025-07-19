package com.divinelink.feature.lists.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.account.FetchUserListsUseCase
import com.divinelink.core.domain.account.UserListsParameters
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListData
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ListsViewModel(private val fetchUserListsUseCase: FetchUserListsUseCase) : ViewModel() {

  private val _uiState: MutableStateFlow<ListsUiState> = MutableStateFlow(ListsUiState.initial)
  val uiState: StateFlow<ListsUiState> = _uiState

  init {
    viewModelScope.launch {
      fetchUserLists()
    }
  }

  fun onLoadMore() {
    val lists = uiState.value.lists

    if (lists is ListData.Data && lists.data.canLoadMore()) {
      fetchUserLists()
    }
  }

  private fun fetchUserLists() {
    _uiState.update { uiState ->
      uiState.copy(
        loadingMore = uiState.lists !is ListData.Initial,
      )
    }
    viewModelScope.launch {
      fetchUserListsUseCase(
        UserListsParameters(
          page = uiState.value.page,
        ),
      )
        .collect { result ->
          result.fold(
            onSuccess = {
              _uiState.update { uiState ->
                uiState.copy(
                  loadingMore = false,
                  isLoading = false,
                  page = uiState.page + 1,
                  error = null,
                  lists = when (uiState.lists) {
                    ListData.Initial -> ListData.Data(result.data)
                    is ListData.Data -> ListData.Data(
                      uiState.lists.data.copy(
                        page = result.data.page,
                        list = buildList {
                          addAll(uiState.lists.data.list)
                          addAll(result.data.list)
                        },
                      ),
                    )
                  },
                )
              }
            },
            onFailure = {
              if (uiState.value.lists is ListData.Initial) {
                ErrorHandler.create(it) {
                  on<SessionException.Unauthenticated> {
                    setUnauthenticatedError()
                  }
                  on<UnknownHostException> {
                    _uiState.update { uiState ->
                      uiState.copy(
                        error = BlankSlateState.Offline,
                        isLoading = false,
                        loadingMore = false,
                      )
                    }
                  }
                  otherwise {
                    _uiState.update { uiState ->
                      uiState.copy(
                        error = BlankSlateState.Generic,
                        isLoading = false,
                        loadingMore = false,
                      )
                    }
                  }
                }
              } else {
                _uiState.update { uiState ->
                  uiState.copy(
                    loadingMore = false,
                    isLoading = false,
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
          UIText.ResourceText(R.string.feature_lists_login_description),
        ),
        page = 1,
        lists = ListData.Initial,
        isLoading = false,
        loadingMore = false,
      )
    }
  }
}
