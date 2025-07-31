package com.divinelink.feature.add.to.account.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.data
import com.divinelink.core.commons.domain.onError
import com.divinelink.core.domain.list.AddItemParameters
import com.divinelink.core.domain.list.AddItemToListUseCase
import com.divinelink.core.domain.list.FetchUserListsUseCase
import com.divinelink.core.domain.list.UserListsParameters
import com.divinelink.core.domain.list.mergeListItems
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.list.ListException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.AddToListRoute
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.add.to.account.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddToListViewModel(
  private val fetchUserListsUseCase: FetchUserListsUseCase,
  private val addItemToListUseCase: AddItemToListUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: AddToListRoute = AddToListRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    mediaType = savedStateHandle.get<MediaType>("mediaType") ?: MediaType.UNKNOWN,
  )

  private val _uiState: MutableStateFlow<AddToListUiState> = MutableStateFlow(
    AddToListUiState.initial(route),
  )
  val uiState: StateFlow<AddToListUiState> = _uiState

  private val _navigateToTMDBAuth = Channel<Unit>()
  val navigateToTMDBAuth: Flow<Unit> = _navigateToTMDBAuth.receiveAsFlow()

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
          result.onSuccess {
            _uiState.update { uiState ->
              uiState.copy(
                loadingMore = false,
                isLoading = false,
                page = result.data.page + 1,
                error = null,
                lists = when (uiState.lists) {
                  ListData.Initial -> ListData.Data(result.data)
                  is ListData.Data -> ListData.Data(
                    uiState.lists.data.copy(
                      page = result.data.page,
                      list = mergeListItems(
                        page = result.data.page - 1,
                        existingItems = uiState.lists.data.list,
                        newItems = result.data.list,
                      ),
                    ),
                  )
                },
              )
            }
          }.onFailure {
            _uiState.update { uiState ->
              uiState.copy(
                isLoading = false,
                loadingMore = false,
              )
            }
          }.onError<SessionException.Unauthenticated> {
            setUnauthenticatedError()
          }
        }
    }
  }

  fun onAction(action: AddToListAction) {
    when (action) {
      AddToListAction.LoadMore -> onLoadMore()
      is AddToListAction.OnListClick -> addToList(action.id)

      AddToListAction.ConsumeDisplayMessage -> _uiState.update { uiState ->
        uiState.copy(
          displayMessage = null,
        )
      }
      AddToListAction.Login -> viewModelScope.launch {
        _navigateToTMDBAuth.send(Unit)
      }
    }
  }

  private fun onLoadMore() {
    val lists = uiState.value.lists

    if (lists is ListData.Data && lists.data.canLoadMore()) {
      fetchUserLists()
    }
  }

  private fun addToList(listId: Int) {
    _uiState.update { uiState ->
      uiState.copy(
        isLoading = true,
        displayMessage = null,
      )
    }
    viewModelScope.launch {
      addItemToListUseCase(
        parameters = AddItemParameters(
          mediaId = uiState.value.id,
          mediaType = uiState.value.mediaType,
          listId = listId,
        ),
      )
        .collect { result ->
          result
            .onSuccess {
              _uiState.update { uiState ->
                val lists = uiState.lists as ListData.Data

                uiState.copy(
                  isLoading = false,
                  displayMessage = DisplayMessage.Success(
                    UIText.ResourceText(
                      R.string.feature_add_to_account_item_added_to_list_success,
                      lists.data.list.first { it.id == listId }.name,
                    ),
                  ),
                )
              }
            }
            .onError<SessionException.Unauthenticated> {
              setUnauthenticatedError()
            }
            .onError<ListException.ItemAlreadyExists> {
              _uiState.update { uiState ->
                uiState.copy(
                  isLoading = false,
                  displayMessage = DisplayMessage.Error(
                    UIText.ResourceText(
                      R.string.feature_add_to_account_item_added_to_list_failure,
                      (uiState.lists as ListData.Data).data.list.first { list ->
                        list.id == listId
                      }.name,
                    ),
                  ),
                )
              }
            }
            .onFailure {
              _uiState.update { uiState ->
                uiState.copy(
                  isLoading = false,
                  displayMessage = DisplayMessage.Error(
                    UIText.ResourceText(
                      R.string.feature_add_to_account_item_add_to_list_unexpected_failure,
                      (uiState.lists as ListData.Data).data.list.first { list ->
                        list.id == listId
                      }.name,
                    ),
                  ),
                )
              }
            }
        }
    }
  }

  private fun setUnauthenticatedError() {
    _uiState.update { uiState ->
      uiState.copy(
        error = BlankSlateState.Unauthenticated(
          description = UIText.ResourceText(R.string.feature_add_to_account_list_login_description),
          retryText = UIText.ResourceText(com.divinelink.core.ui.R.string.core_ui_login),
        ),
        page = 1,
        lists = ListData.Initial,
        isLoading = false,
        loadingMore = false,
      )
    }
  }
}
