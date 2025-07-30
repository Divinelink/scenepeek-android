package com.divinelink.feature.add.to.account.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.shareUrl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ActionMenuViewModel(
  entryPoint: ActionMenuEntryPoint,
  mediaItem: MediaItem,
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
      ActionMenuIntent.RemoveFromList -> {
        // TODO Implement
      }
      ActionMenuIntent.MultiSelect -> {
        // Do nothing
      }
    }
  }
}
