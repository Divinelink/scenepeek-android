package com.divinelink.feature.lists

import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ListsViewModelTestRobot : ViewModelTestRobot<ListsUiState>() {

  private lateinit var viewModel: ListsViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ListsViewModel()
  }

  override val actualUiState: Flow<ListsUiState>
    get() = viewModel.uiState

  fun assertUiState(expectedUiState: ListsUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }
}
