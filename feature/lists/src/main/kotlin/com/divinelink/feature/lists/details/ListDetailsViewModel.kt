package com.divinelink.feature.lists.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.ErrorHandler
import com.divinelink.core.domain.list.FetchListDetailsUseCase
import com.divinelink.core.domain.list.FetchListParameters
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.navigation.route.ListDetailsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListDetailsViewModel(
  private val fetchListDetailsUseCase: FetchListDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: ListDetailsRoute = ListDetailsRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    name = savedStateHandle.get<String>("name") ?: "",
  )

  private val _uiState: MutableStateFlow<ListDetailsUiState> = MutableStateFlow(
    ListDetailsUiState.initial(
      id = route.id,
      name = route.name,
    ),
  )
  val uiState: StateFlow<ListDetailsUiState> = _uiState

  init {
    fetchListDetails()
  }

  private fun fetchListDetails() {
    viewModelScope.launch {
      fetchListDetailsUseCase.invoke(
        FetchListParameters(
          listId = uiState.value.id,
          page = uiState.value.page,
        ),
      ).collect { result ->
        result.fold(
          onSuccess = { listDetails ->
            _uiState.update { uiState ->
              uiState.copy(
                page = listDetails.page,
                details = when (uiState.details) {
                  ListDetailsData.Initial -> ListDetailsData.Data(
                    data = listDetails,
                  )
                  is ListDetailsData.Data -> ListDetailsData.Data(
                    data = listDetails.copy(media = uiState.details.data.media + listDetails.media),
                  )
                },
              )
            }
          },
          onFailure = { error ->
            ErrorHandler.create(error) {
              // Todo handle error appropriately
            }
          },
        )
      }
    }
  }
}
