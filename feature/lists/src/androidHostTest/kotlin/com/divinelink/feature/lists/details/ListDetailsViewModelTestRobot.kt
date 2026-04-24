package com.divinelink.feature.lists.details

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.usecase.TestFetchListDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ListDetailsViewModelTestRobot : ViewModelTestRobot<ListDetailsUiState>() {

  private lateinit var viewModel: ListDetailsViewModel
  private lateinit var navArgs: Navigation.ListDetailsRoute

  private val fetchListDetailsUseCase = TestFetchListDetailsUseCase()
  private val repository = TestListRepository()
  private val authRepository = TestAuthRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ListDetailsViewModel(
      route = navArgs,
      fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
      repository = repository.mock,
      authRepository = authRepository.mock,
    )
  }

  fun withArgs(args: Navigation.ListDetailsRoute) = apply {
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

  fun mockTMDBAccount(accountDetails: AccountDetails?) = apply {
    authRepository.mockTMDBAccount(accountDetails)
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
