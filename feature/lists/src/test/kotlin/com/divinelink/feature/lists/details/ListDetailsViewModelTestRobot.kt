package com.divinelink.feature.lists.details

import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.usecase.TestFetchListDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ListDetailsViewModelTestRobot : ViewModelTestRobot<ListDetailsUiState>() {

  private lateinit var viewModel: ListDetailsViewModel
  private lateinit var navArgs: ListDetailsRoute

  private val fetchListDetailsUseCase = TestFetchListDetailsUseCase()
  private val repository = TestListRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ListDetailsViewModel(
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      repository = repository.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "name" to navArgs.name,
          "backdropPath" to navArgs.backdropPath,
          "description" to navArgs.description,
          "public" to navArgs.public,
        ),
      ),
    )
  }

  fun withArgs(args: ListDetailsRoute) = apply {
    this.navArgs = args
  }

  fun onLoadMore() = apply {
    viewModel.onAction(ListDetailsAction.LoadMore)
  }

  fun onRefresh() = apply {
    viewModel.onAction(ListDetailsAction.Refresh)
  }

  fun onSelectMedia(media: MediaItem.Media) = apply {
    viewModel.onAction(ListDetailsAction.SelectMedia(media))
  }

  fun onSelectAll() = apply {
    viewModel.onAction(ListDetailsAction.OnSelectAll)
  }

  fun onDeselectAll() = apply {
    viewModel.onAction(ListDetailsAction.OnDeselectAll)
  }

  fun onRemoveItems() = apply {
    viewModel.onAction(ListDetailsAction.OnRemoveItems)
  }

  fun onConsumeSnackbarMessage() = apply {
    viewModel.onAction(ListDetailsAction.ConsumeSnackbarMessage)
  }

  override val actualUiState: Flow<ListDetailsUiState>
    get() = viewModel.uiState

  fun assertUiState(expectedUiState: ListDetailsUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  fun mockListDetails(response: Result<ListDetails>) = apply {
    fetchListDetailsUseCase.mockResponse(response)
  }

  fun mockListDetails(response: Channel<Result<ListDetails>>) = apply {
    fetchListDetailsUseCase.mockResponse(response)
  }

  suspend fun mockRemoveItems(response: Result<Int>) = apply {
    repository.mockRemoveItems(response)
  }
}
