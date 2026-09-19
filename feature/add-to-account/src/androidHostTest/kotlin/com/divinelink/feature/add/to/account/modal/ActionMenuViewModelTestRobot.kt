package com.divinelink.feature.add.to.account.modal

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.repository.TestTrackRepository
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ActionMenuViewModelTestRobot : ViewModelTestRobot<ActionMenuUiState>() {

  private val listRepository = TestListRepository()
  private val detailsRepository = TestDetailsRepository()
  private val trackRepository = TestTrackRepository()
  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
  private lateinit var viewModel: ActionMenuViewModel
  private lateinit var entryPoint: ActionMenuEntryPoint

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ActionMenuViewModel(
      entryPoint = entryPoint,
      listRepository = listRepository.mock,
      detailsRepository = detailsRepository.mock,
      trackRepository = trackRepository.mock,
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )
  }

  override val actualUiState: Flow<ActionMenuUiState>
    get() = viewModel.uiState

  fun setup(entryPoint: ActionMenuEntryPoint) = apply {
    this@ActionMenuViewModelTestRobot.entryPoint = entryPoint

    buildViewModel()
  }

  fun assertUiState(expectedUiState: ActionMenuUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  fun mockMarkAsFavorite(
    mediaItem: MediaItem.Media,
    result: Result<Boolean>,
  ) = apply {
    markAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = mediaItem,
      result = result,
    )
  }

  suspend fun mockRemoveFromList(result: Result<Int>) = apply {
    listRepository.mockRemoveItems(result)
  }

  fun onMarkAsFavorite() = apply {
    viewModel.onMarkAsFavorite()
  }

  fun onRemoveItemsFromList() = apply {
    viewModel.onAction(ActionMenuSection.Intent.RemoveFromList)
  }

  fun onDismissSnackbar() = apply {
    viewModel.onDismissSnackbar()
  }

  fun mockFetchMediaItem(response: Flow<Resource<MediaItem.Media?>>) = apply {
    detailsRepository.mockFetchMediaItem(response)
  }

  fun mockFetchMediaItem(response: Channel<Resource<MediaItem.Media?>>) = apply {
    detailsRepository.mockFetchMediaItem(response)
  }
}
