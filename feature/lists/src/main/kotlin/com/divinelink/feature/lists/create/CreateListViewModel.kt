package com.divinelink.feature.lists.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.domain.list.CreateListParameters
import com.divinelink.core.domain.list.CreateListUseCase
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.lists.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateListViewModel(
  private val createListUseCase: CreateListUseCase,
  private val repository: ListRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: Any = if (savedStateHandle.get<Int>("id") != null) {
    Navigation.EditListRoute(
      id = savedStateHandle.get<Int>("id") ?: 0,
      name = savedStateHandle.get<String>("name") ?: "",
      backdropPath = savedStateHandle.get<String>("backdropPath") ?: "",
      description = savedStateHandle.get<String>("description") ?: "",
      public = savedStateHandle.get<Boolean>("public") ?: false,
    )
  } else {
    Navigation.CreateListRoute
  }

  private val _uiState: MutableStateFlow<CreateListUiState> = MutableStateFlow(
    if (route is Navigation.EditListRoute) {
      CreateListUiState.initial.copy(
        id = route.id,
        name = route.name,
        description = route.description,
        backdrop = route.backdropPath ?: "",
        public = route.public,
        editMode = true,
      )
    } else {
      CreateListUiState.initial
    },
  )
  val uiState: StateFlow<CreateListUiState> = _uiState

  private val _onNavigateUp = Channel<Unit>()
  val onNavigateUp: Flow<Unit> = _onNavigateUp.receiveAsFlow()

  private val _onNavigateBackToLists = Channel<Unit>()
  val onNavigateBackToLists: Flow<Unit> = _onNavigateBackToLists.receiveAsFlow()

  fun onAction(action: CreateListAction) {
    when (action) {
      CreateListAction.CreateOrEditList -> if (uiState.value.editMode) {
        editList()
      } else {
        createList()
      }
      CreateListAction.DeleteList -> deleteList()
      is CreateListAction.NameChanged -> _uiState.update {
        it.copy(name = action.name)
      }
      is CreateListAction.DescriptionChanged -> _uiState.update {
        it.copy(description = action.description)
      }
      is CreateListAction.BackdropChanged -> _uiState.update {
        it.copy(backdrop = action.path)
      }
      is CreateListAction.PublicChanged -> _uiState.update {
        it.copy(public = !it.public)
      }
      CreateListAction.DismissSnackbar -> _uiState.update {
        it.copy(snackbarMessage = null)
      }
    }
  }

  private fun deleteList() {
    _uiState.update { state ->
      state.copy(
        loading = true,
      )
    }

    viewModelScope.launch {
      repository
        .deleteList(uiState.value.id)
        .onSuccess {
          _uiState.update { state ->
            state.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.feature_lists_delete_successfully,
                  uiState.value.name,
                ),
              ),
              loading = false,
            )
          }
          _onNavigateBackToLists.send(Unit)
        }
        .onFailure {
          _uiState.update { state ->
            state.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.feature_lists_delete_failure,
                  uiState.value.name,
                ),
              ),
              loading = false,
            )
          }
        }
    }
  }

  private fun editList() {
    _uiState.update { state ->
      state.copy(
        loading = true,
      )
    }

    viewModelScope.launch {
      repository.updateList(
        listId = uiState.value.id,
        request = UpdateListRequest.create(
          name = uiState.value.name,
          description = uiState.value.description,
          public = uiState.value.public,
          backdrop = uiState.value.backdrop?.ifBlank { null },
        ),
      )
        .onSuccess {
          _uiState.update { state ->
            state.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.feature_lists_update_successfully,
                  uiState.value.name,
                ),
              ),
              loading = false,
            )
          }
          _onNavigateUp.send(Unit)
        }
        .onFailure {
          _uiState.update { state ->
            state.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.feature_lists_update_failure,
                  uiState.value.name,
                ),
              ),
              loading = false,
            )
          }
        }
    }
  }

  private fun createList() {
    viewModelScope.launch {
      createListUseCase.invoke(
        CreateListParameters(
          name = _uiState.value.name,
          description = _uiState.value.description,
          public = _uiState.value.public,
        ),
      )
        .distinctUntilChanged()
        .collect {
          it.fold(
            onSuccess = {
              _uiState.update { state ->
                state.copy(
                  snackbarMessage = SnackbarMessage.from(
                    text = UIText.ResourceText(
                      R.string.feature_lists_create_successfully,
                      uiState.value.name,
                    ),
                  ),
                )
              }

              _onNavigateUp.send(Unit)
            },
            onFailure = {
              _uiState.update { state ->
                state.copy(
                  snackbarMessage = SnackbarMessage.from(
                    text = UIText.ResourceText(
                      R.string.feature_lists_create_failure,
                      uiState.value.name,
                    ),
                  ),
                )
              }
            },
          )
        }
    }
  }
}
