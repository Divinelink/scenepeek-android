package com.divinelink.feature.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.account.FetchUserListsUseCase
import com.divinelink.core.domain.account.UserListsParameters
import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListsViewModel(
  private val observeAccountUseCase: ObserveAccountUseCase,
  private val fetchUserListsUseCase: FetchUserListsUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<ListsUiState> = MutableStateFlow(ListsUiState.initial)
  val uiState: StateFlow<ListsUiState> = _uiState

  init {
    viewModelScope.launch {
      observeAccountUseCase.invoke(Unit)
        .collectLatest { result ->
          result.fold(
            onSuccess = {
              fetchUserLists()
            },
            onFailure = {
              setUnauthenticatedError()
            },
          )
        }
    }
  }

  fun onLoadMore() {
    val lists = uiState.value.lists

    if (lists is ListData.Data && lists.data.canLoadMore()) {
      fetchUserLists()
    }
  }

  private fun fetchUserLists() {
    viewModelScope.launch {
      fetchUserListsUseCase(
        UserListsParameters(
          page = uiState.value.page,
        ),
      ).collect { result ->
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
                      list = uiState.lists.data.list + result.data.list,
                    ),
                  )
                },
              )
            }
          },
          onFailure = {
            ErrorHandler.create(it) {
              on<SessionException.Unauthenticated> {
                setUnauthenticatedError()
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
