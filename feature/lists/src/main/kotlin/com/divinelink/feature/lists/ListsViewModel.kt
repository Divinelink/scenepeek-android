package com.divinelink.feature.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
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
              _uiState.update { uiState ->
                uiState.copy(
                  error = BlankSlateState.Unauthenticated(
                    UIText.ResourceText(R.string.feature_lists_login_description),
                  ),
                  isLoading = false,
                  loadingMore = false,
                  page = 1,
                )
              }
            },
          )
        }
    }
  }

  fun onLoadMore() {
    fetchUserLists()
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
              )
            }
          },
          onFailure = {
            ErrorHandler.create(it) {
              on<SessionException.Unauthenticated> {
                _uiState.update { uiState ->
                  uiState.copy(
                    error = BlankSlateState.Unauthenticated(
                      UIText.ResourceText(R.string.feature_lists_login_description),
                    ),
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
}
