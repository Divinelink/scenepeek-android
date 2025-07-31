package com.divinelink.feature.add.to.account.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.shareUrl
import com.divinelink.core.model.media.toStub
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.divinelink.core.ui.R as uiR

class ActionMenuViewModel(
  entryPoint: ActionMenuEntryPoint,
  mediaItem: MediaItem,
  private val listRepository: ListRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<ActionMenuUiState> = MutableStateFlow(
    ActionMenuUiState.initial(
      media = mediaItem,
      entryPoint = entryPoint,
    ),
  )
  val uiState: StateFlow<ActionMenuUiState> = _uiState

  private val _shareUrl = Channel<String>()
  val shareUrl: Flow<String> = _shareUrl.receiveAsFlow()

  private val _addToList = Channel<MediaItem>()
  val addToList: Flow<MediaItem> = _addToList.receiveAsFlow()

  fun onAction(intent: ActionMenuIntent) {
    when (intent) {
      ActionMenuIntent.Share -> viewModelScope.launch {
        _shareUrl.send(_uiState.value.media.shareUrl())
      }
      ActionMenuIntent.AddToList -> viewModelScope.launch {
        _addToList.send(_uiState.value.media)
      }
      ActionMenuIntent.RemoveFromList -> removeItemFromList()
      ActionMenuIntent.MultiSelect -> {
        // Do nothing
      }
    }
  }

  fun onDismissSnackbar() {
    _uiState.update {
      it.copy(snackbarMessage = null)
    }
  }

  private fun removeItemFromList() {
    if (uiState.value.entryPoint !is ActionMenuEntryPoint.ListDetails) return

    viewModelScope.launch {
      listRepository.removeItems(
        listId = (uiState.value.entryPoint as ActionMenuEntryPoint.ListDetails).listId,
        items = listOf(_uiState.value.media.toStub()),
      ).onSuccess {
        _uiState.update {
          it.copy(
            snackbarMessage = SnackbarMessage.from(
              UIText.ResourceText(
                R.string.feature_add_to_account_remove_single_item_success,
                _uiState.value.media.name,
                (_uiState.value.entryPoint as ActionMenuEntryPoint.ListDetails).listName,
              ),
            ),
          )
        }
      }
        .onFailure { error ->
          when (error) {
            is AppException.Offline -> _uiState.update {
              it.copy(
                snackbarMessage = SnackbarMessage.from(
                  UIText.ResourceText(
                    R.string.feature_add_to_account_remove_from_list_offline_error,
                  ),
                ),
              )
            }
            else -> _uiState.update {
              it.copy(
                snackbarMessage = error.message?.let { message ->
                  SnackbarMessage.from(UIText.StringText(message))
                } ?: SnackbarMessage.from(UIText.ResourceText(uiR.string.core_ui_error_retry)),
              )
            }
          }
        }
    }
  }
}
