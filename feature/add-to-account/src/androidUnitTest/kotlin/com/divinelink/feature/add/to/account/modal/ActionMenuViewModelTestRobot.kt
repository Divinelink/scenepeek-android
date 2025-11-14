package com.divinelink.feature.add.to.account.modal

import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuIntent
import com.divinelink.feature.add.to.account.modal.ActionMenuUiState
import com.divinelink.feature.add.to.account.modal.ActionMenuViewModel
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ActionMenuViewModelTestRobot : ViewModelTestRobot<ActionMenuUiState>() {

  private val listRepository = TestListRepository()
  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
  private lateinit var viewModel: ActionMenuViewModel
  private lateinit var entryPoint: ActionMenuEntryPoint
  private lateinit var mediaItem: MediaItem

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ActionMenuViewModel(
      entryPoint = entryPoint,
      mediaItem = mediaItem,
      listRepository = listRepository.mock,
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )
  }

  override val actualUiState: Flow<ActionMenuUiState>
    get() = viewModel.uiState

  fun setup(
    mediaItem: MediaItem,
    entryPoint: ActionMenuEntryPoint,
  ) = apply {
    this@ActionMenuViewModelTestRobot.mediaItem = mediaItem
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
    viewModel.onAction(ActionMenuIntent.RemoveFromList)
  }

  fun onDismissSnackbar() = apply {
    viewModel.onDismissSnackbar()
  }
}
