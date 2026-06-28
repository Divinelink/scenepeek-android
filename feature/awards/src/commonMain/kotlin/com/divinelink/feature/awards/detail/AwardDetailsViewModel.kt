package com.divinelink.feature.awards.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.awards.AwardsRepository
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.blankslate.toBlankSlateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AwardDetailsViewModel(
  route: Navigation.AwardDetailsRoute,
  private val awardsRepository: AwardsRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<AwardDetailsUiState> = MutableStateFlow(
    AwardDetailsUiState.initial(route.ceremony),
  )
  val uiState: StateFlow<AwardDetailsUiState> = _uiState

  init {
    fetchCategories()
  }

  fun onAction(action: AwardDetailsAction) {
    when (action) {
      is AwardDetailsAction.OnCategoryClick -> Unit
      AwardDetailsAction.OnRetry -> fetchCategories()
    }
  }

  private fun fetchCategories() {
    viewModelScope.launch {
      _uiState.update { state ->
        state.copy(
          loading = true,
          error = null,
        )
      }

      awardsRepository
        .fetchCeremonyCategories(id = uiState.value.ceremony.id)
        .fold(
          onSuccess = { result ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = null,
                categories = result,
              )
            }
          },
          onFailure = { error ->
            _uiState.update { state ->
              state.copy(
                loading = false,
                error = error.toBlankSlateState(),
              )
            }
          },
        )
    }
  }
}
