package com.divinelink.feature.add.to.account.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.data.track.TrackRepository
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.shareUrl
import com.divinelink.core.model.media.toStub
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.network.Resource
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_from_list_offline_error
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_single_item_success
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActionMenuViewModel(
  entryPoint: ActionMenuEntryPoint,
  private val detailsRepository: DetailsRepository,
  private val listRepository: ListRepository,
  private val trackRepository: TrackRepository,
  private val markAsFavoriteUseCase: MarkAsFavoriteUseCase,
) : ViewModel() {

  private val _uiState: MutableStateFlow<ActionMenuUiState> = MutableStateFlow(
    ActionMenuUiState.initial(
      entryPoint = entryPoint,
    ),
  )
  val uiState: StateFlow<ActionMenuUiState> = _uiState

  private val _shareUrl = Channel<String>()
  val shareUrl: Flow<String> = _shareUrl.receiveAsFlow()

  private val _addToList = Channel<MediaItem>()
  val addToList: Flow<MediaItem> = _addToList.receiveAsFlow()

  init {
    when (entryPoint) {
      is ActionMenuEntryPoint.ShowProgress -> fetchMediaItem(entryPoint.showId)
      is ActionMenuEntryPoint.ListDetails,
      is ActionMenuEntryPoint.Other,
        -> Unit
    }
  }

  fun onAction(intent: ActionMenuSection.Intent) {
    when (intent) {
      ActionMenuSection.Intent.Share -> uiState.value.media?.let { media ->
        viewModelScope.launch {
          _shareUrl.send(media.shareUrl(domain = "themoviedb.org"))
        }
      }
      ActionMenuSection.Intent.AddToList -> _uiState.value.media?.let {
        viewModelScope.launch { _addToList.send(it) }
      }
      ActionMenuSection.Intent.RemoveFromList -> removeItemFromList()
      ActionMenuSection.Intent.MultiSelect -> Unit
      ActionMenuSection.Intent.PauseShow -> updateShowStatus(UserShowStatus.ON_HOLD)
      ActionMenuSection.Intent.PlanToWatch -> updateShowStatus(UserShowStatus.PLAN_TO_WATCH)
      ActionMenuSection.Intent.ContinueWatching -> updateShowStatus(UserShowStatus.WATCHING)
      ActionMenuSection.Intent.DropShow -> updateShowStatus(UserShowStatus.DROPPED)
    }
  }

  private fun updateShowStatus(status: UserShowStatus) = viewModelScope.launch {
    val showId = (uiState.value.entryPoint as? ActionMenuEntryPoint.ShowProgress)?.showId
      ?: return@launch

    trackRepository.updateShowStatus(showId, status)
  }

  fun onMarkAsFavorite() {
    if (uiState.value.media !is MediaItem.Media) return

    viewModelScope.launch {
      markAsFavoriteUseCase(uiState.value.media as MediaItem.Media)
        .onSuccess { saved ->
          _uiState.update { uiState ->
            val media = when (val media = uiState.media) {
              is MediaItem.Media.Movie -> media.copy(saved = saved)
              is MediaItem.Media.TV -> media.copy(saved = saved)
              null -> return@onSuccess
              else -> media
            }

            uiState.updateMedia(media)
          }
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
    val media = uiState.value.media ?: return

    viewModelScope.launch {
      listRepository.removeItems(
        listId = (uiState.value.entryPoint as ActionMenuEntryPoint.ListDetails).listId,
        items = listOf(media.toStub()),
      ).onSuccess {
        _uiState.update {
          it.copy(
            snackbarMessage = SnackbarMessage.from(
              UIText.ResourceText(
                Res.string.feature_add_to_account_remove_single_item_success,
                media.name,
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
                    Res.string.feature_add_to_account_remove_from_list_offline_error,
                  ),
                ),
              )
            }
            else -> _uiState.update {
              it.copy(
                snackbarMessage = error.message?.let { message ->
                  SnackbarMessage.from(UIText.StringText(message))
                } ?: SnackbarMessage.from(UIText.ResourceText(UiString.core_ui_error_retry)),
              )
            }
          }
        }
    }
  }

  private fun fetchMediaItem(showId: Long) {
    viewModelScope.launch {
      detailsRepository
        .fetchMediaItem(
          media = MediaReference(
            mediaId = showId,
            mediaType = MediaType.TV,
          ),
        )
        .collect { result ->
          when (result) {
            is Resource.Error -> Unit
            is Resource.Loading -> result.data?.let { media ->
              _uiState.update { state ->
                state.copy(
                  mediaState = ItemState.Data(
                    item = media,
                    loading = false,
                  ),
                )
              }
            }
            is Resource.Success -> result.data?.let { media ->
              _uiState.update { state ->
                state.copy(
                  mediaState = ItemState.Data(
                    item = media,
                    loading = false,
                  ),
                )
              }
            }
          }
        }
    }
  }
}
