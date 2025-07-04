package com.divinelink.feature.add.to.account.list

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
import com.divinelink.feature.add.to.account.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddToListViewModel(private val fetchUserListsUseCase: FetchUserListsUseCase) : ViewModel() {
  private val _uiState: MutableStateFlow<AddToListUiState> = MutableStateFlow(
    AddToListUiState.initial,
  )
  val uiState: StateFlow<AddToListUiState> = _uiState

  init {
    fetchUserLists()
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
              ErrorHandler.create(it) {
                on<SessionException.Unauthenticated> {
                  setUnauthenticatedError()
                }
                otherwise {
                  _uiState.update { uiState ->
                    uiState.copy(
                      isLoading = false,
                      loadingMore = false,
                    )
                  }
                }
              }
            },
          )
        }
    }
  }

  fun onUserInteraction(userInteraction: AddToListUserInteraction) {
    when (userInteraction) {
      is AddToListUserInteraction.LoadMore -> {
        if (_uiState.value.lists is ListData.Data && !_uiState.value.loadingMore) {
          fetchUserLists()
        }
      }
      is AddToListUserInteraction.OnListClick -> {
      }
    }
  }

  private fun setUnauthenticatedError() {
    _uiState.update { uiState ->
      uiState.copy(
        error = BlankSlateState.Unauthenticated(
          UIText.ResourceText(R.string.feature_add_to_account_list_login_description),
        ),
        page = 1,
        lists = ListData.Initial,
        isLoading = false,
        loadingMore = false,
      )
    }
  }
}
