@file:Suppress("LongMethod")

package com.divinelink.feature.lists.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.onError
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.domain.list.FetchListDetailsUseCase
import com.divinelink.core.domain.list.FetchListParameters
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.toStub
import com.divinelink.core.navigation.route.Navigation.ListDetailsRoute
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_batch_items_success
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_from_list_offline_error
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_single_item_success
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListDetailsViewModel(
  private val fetchListDetailsUseCase: FetchListDetailsUseCase,
  private val repository: ListRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: ListDetailsRoute = ListDetailsRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    name = savedStateHandle.get<String>("name") ?: "",
    backdropPath = savedStateHandle.get<String>("backdropPath") ?: "",
    description = savedStateHandle.get<String>("description") ?: "",
    public = savedStateHandle.get<Boolean>("public") ?: false,
  )

  private val _uiState: MutableStateFlow<ListDetailsUiState> = MutableStateFlow(
    ListDetailsUiState.initial(
      id = route.id,
      name = route.name,
      backdropPath = route.backdropPath,
      description = route.description,
      public = route.public,
    ),
  )
  val uiState: StateFlow<ListDetailsUiState> = _uiState

  init {
    fetchListDetails(isRefreshing = false)
  }

  private fun fetchListDetails(isRefreshing: Boolean) {
    _uiState.update { uiState ->
      uiState.copy(
        loadingMore = uiState.details !is ListDetailsData.Initial && !isRefreshing,
      )
    }

    viewModelScope.launch {
      if (isRefreshing) {
        delay(250)
      }

      fetchListDetailsUseCase.invoke(
        FetchListParameters(
          listId = uiState.value.id,
          page = if (isRefreshing) 1 else uiState.value.page,
        ),
      )
        .distinctUntilChanged()
        .collect { result ->
          result
            .onSuccess { listDetails ->
              _uiState.update { uiState ->
                uiState.copy(
                  page = listDetails.page + 1,
                  details = if (isRefreshing) {
                    ListDetailsData.Data(
                      data = listDetails,
                      pages = mapOf(1 to listDetails.media),
                    )
                  } else {
                    when (uiState.details) {
                      is ListDetailsData.Initial -> ListDetailsData.Data(
                        data = listDetails,
                        pages = mapOf(1 to listDetails.media),
                      )
                      is ListDetailsData.Data -> ListDetailsData.Data(
                        data = listDetails,
                        pages = uiState.details.pages + (listDetails.page to listDetails.media),
                      )
                    }
                  },
                  loadingMore = false,
                  refreshing = false,
                  error = null,
                )
              }
            }
            .onError<AppException.Offline> {
              if (uiState.value.details is ListDetailsData.Initial) {
                _uiState.update { uiState ->
                  uiState.copy(
                    error = BlankSlateState.Offline,
                    loadingMore = false,
                    refreshing = false,
                  )
                }
              }
            }
            .onFailure {
              if (uiState.value.details is ListDetailsData.Initial) {
                _uiState.update { uiState ->
                  uiState.copy(
                    error = BlankSlateState.Generic,
                    loadingMore = false,
                    refreshing = false,
                  )
                }
              } else {
                _uiState.update { uiState ->
                  uiState.copy(
                    loadingMore = false,
                    refreshing = false,
                  )
                }
              }
            }
        }
    }
  }

  fun onAction(action: ListDetailsAction) {
    when (action) {
      ListDetailsAction.LoadMore -> if (_uiState.value.canLoadMore()) {
        fetchListDetails(isRefreshing = false)
      }
      ListDetailsAction.Refresh -> refresh()
      is ListDetailsAction.OnItemClick -> {
        // Do nothing
      }
      is ListDetailsAction.SelectMedia -> _uiState.update { uiState ->
        val reference = action.media
        if (uiState.selectedMedia.contains(reference)) {
          uiState.copy(
            selectedMedia = uiState.selectedMedia - reference,
          )
        } else {
          uiState.copy(
            multipleSelectMode = true,
            selectedMedia = uiState.selectedMedia + reference,
          )
        }
      }
      ListDetailsAction.OnDeselectAll -> _uiState.update {
        it.copy(
          selectedMedia = emptyList(),
        )
      }
      ListDetailsAction.OnSelectAll -> _uiState.update { uiState ->
        if (uiState.details is ListDetailsData.Data) {
          uiState.copy(
            selectedMedia = uiState.details.media,
          )
        } else {
          uiState
        }
      }
      ListDetailsAction.OnDismissMultipleSelect -> _uiState.update { uiState ->
        uiState.copy(
          multipleSelectMode = false,
          selectedMedia = emptyList(),
        )
      }
      ListDetailsAction.OnRemoveItems -> onRemoveItems(uiState.value.selectedMedia)
      ListDetailsAction.ConsumeSnackbarMessage -> _uiState.update { uiState ->
        uiState.copy(
          snackbarMessage = null,
        )
      }
    }
  }

  private fun refresh() {
    _uiState.update { uiState ->
      uiState.copy(
        refreshing = true,
      )
    }
    fetchListDetails(isRefreshing = true)
  }

  private fun onRemoveItems(items: List<MediaItem.Media>) {
    viewModelScope.launch {
      repository.removeItems(
        listId = _uiState.value.id,
        items = items.map { it.toStub() },
      ).fold(
        onSuccess = { result ->
          _uiState.update {
            val snackbarMessage = when (result) {
              0 -> null
              1 -> SnackbarMessage.from(
                UIText.ResourceText(
                  Res.string.feature_add_to_account_remove_single_item_success,
                  items.first().name,
                  uiState.value.details.name,
                ),
              )
              else -> SnackbarMessage.from(
                UIText.ResourceText(
                  Res.string.feature_add_to_account_remove_batch_items_success,
                  items.size,
                  uiState.value.details.name,
                ),
              )
            }
            it.copy(
              multipleSelectMode = false,
              selectedMedia = emptyList(),
              snackbarMessage = snackbarMessage,
            )
          }
        },
        onFailure = { error ->
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
        },
      )
    }
  }
}
