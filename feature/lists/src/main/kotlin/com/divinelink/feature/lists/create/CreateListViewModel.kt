package com.divinelink.feature.lists.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.domain.list.CreateListParameters
import com.divinelink.core.domain.list.CreateListUseCase
import com.divinelink.core.model.UIText
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

class CreateListViewModel(private val createListUseCase: CreateListUseCase) : ViewModel() {

  private val _uiState: MutableStateFlow<CreateListUiState> = MutableStateFlow(
    CreateListUiState.initial,
  )
  val uiState: StateFlow<CreateListUiState> = _uiState

  private val _onNavigateUp = Channel<Unit>()
  val onNavigateUp: Flow<Unit> = _onNavigateUp.receiveAsFlow()

  fun onAction(action: CreateListAction) {
    when (action) {
      CreateListAction.CreateOrEditList -> createList()
      CreateListAction.DeleteList -> {
        // Perform the action to delete list
      }
      is CreateListAction.NameChanged -> _uiState.update {
        it.copy(name = action.name)
      }
      is CreateListAction.DescriptionChanged -> _uiState.update {
        it.copy(description = action.description)
      }
      is CreateListAction.PublicChanged -> _uiState.update {
        it.copy(public = !it.public)
      }
      CreateListAction.DismissSnackbar -> _uiState.update {
        it.copy(snackbarMessage = null)
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
