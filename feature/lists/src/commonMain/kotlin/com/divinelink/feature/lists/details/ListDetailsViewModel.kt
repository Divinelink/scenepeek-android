@file:Suppress("LongMethod")

package com.divinelink.feature.lists.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.toStub
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.navigation.route.Navigation.ListDetailsRoute
import com.divinelink.core.network.Resource
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.resources.core_ui_error_retry
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_batch_items_success
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_from_list_offline_error
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_single_item_success
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ListDetailsViewModel(
  route: ListDetailsRoute,
  private val repository: ListRepository,
  private val authRepository: AuthRepository,
  preferencesRepository: PreferencesRepository,
) : ViewModel() {

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

  private var fetchJob: Job? = null
  private var currentFetchKey: String? = null

  init {
    preferencesRepository
      .listSortOption(uiState.value.id)
      .distinctUntilChanged()
      .onEach { sortOption ->
        val previousSort = _uiState.value.sortOption
        val isSortChange = sortOption != previousSort

        if (isSortChange) {
          fetchJob?.cancel()

          _uiState.update { state ->
            state.copy(
              details = ListDetailsData.Initial(
                name = state.details.name,
                backdropPath = state.details.backdropPath,
                description = state.details.description,
                public = state.details.public,
              ),
              updatingSort = true,
            )
          }

          repository.clearList(uiState.value.id.toLong())
        }

        _uiState.update { it.copy(sortOption = sortOption) }

        fetchListDetails(
          page = 1,
          sortBy = sortOption,
          cancelExisting = true,
        )
      }
      .launchIn(viewModelScope)
  }

  @OptIn(ExperimentalUuidApi::class)
  private fun fetchListDetails(page: Int, sortBy: SortOption?, cancelExisting: Boolean) {
    val state = _uiState.value

    /**
     * Update key only when cancelExisting is true to make sure old emissions are not
     * applied to the UI.
     */
    val key = if (cancelExisting) {
      fetchJob?.cancel()
      Uuid.random().toHexDashString()
    } else {
      currentFetchKey
    }

    currentFetchKey = key
    fetchJob = viewModelScope.launch {
      _uiState.update { it.copy(loadingMore = it.details is ListDetailsData.Data && page > 1) }

      repository
        .fetchListDetails(listId = state.id, page = page, sortOption = sortBy)
        .catch { e -> if (e is CancellationException) throw e }
        .distinctUntilChanged()
        .filter { currentFetchKey == key }
        .onEach { result ->
          _uiState.update { state ->
            when (result) {
              is Resource.Success -> result.data?.let {
                state.applyPageData(
                  data = it,
                  loadingMore = false,
                  username = authRepository.tmdbAccount.firstOrNull()?.username,
                )
              } ?: state
              is Resource.Loading -> result.data?.let {
                state.applyPageData(
                  data = it,
                  loadingMore = true,
                  username = authRepository.tmdbAccount.firstOrNull()?.username,
                )
              } ?: state
              is Resource.Error -> {
                state.copy(
                  error = when {
                    result.error is AppException.Offline -> BlankSlateState.Offline
                    uiState.value.details is ListDetailsData.Initial -> BlankSlateState.Generic
                    else -> null
                  },
                  loadingMore = false,
                  refreshing = false,
                )
              }
            }
          }
        }
        .collect()
    }
  }

  fun onAction(action: ListDetailsAction) {
    when (action) {
      ListDetailsAction.LoadMore -> if (_uiState.value.canLoadMore()) {
        val state = _uiState.value
        fetchListDetails(
          page = state.page,
          sortBy = state.sortOption,
          cancelExisting = false,
        )
      }
      ListDetailsAction.Refresh -> refresh()
      is ListDetailsAction.OnItemClick -> Unit
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
    val state = _uiState.value
    _uiState.update { it.copy(refreshing = true) }
    fetchListDetails(
      page = 1,
      sortBy = state.sortOption,
      cancelExisting = true,
    )
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

  private fun ListDetailsUiState.applyPageData(
    data: ListDetails,
    loadingMore: Boolean,
    username: String?,
  ): ListDetailsUiState = copy(
    page = data.page + 1,
    details = if (updatingSort) {
      details
    } else {
      when (val current = details) {
        is ListDetailsData.Initial -> ListDetailsData.Data(
          data = data,
          pages = mapOf(data.page to data.media),
        )
        is ListDetailsData.Data -> ListDetailsData.Data(
          data = data,
          pages = current.pages + (data.page to data.media),
        )
      }
    },
    loadingMore = loadingMore,
    refreshing = false,
    canEdit = username == data.createdBy.username,
    updatingSort = false,
    error = null,
  )
}
