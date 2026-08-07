package com.divinelink.feature.lists.details

import com.divinelink.core.data.preferences.ProdPreferencesRepository
import com.divinelink.core.datastore.ui.DatastoreUiStorage
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ListDetailsViewModelTestRobot : ViewModelTestRobot<ListDetailsUiState>() {

  private lateinit var viewModel: ListDetailsViewModel
  private lateinit var navArgs: Navigation.ListDetailsRoute

  private val repository = TestListRepository()
  private val authRepository = TestAuthRepository()

  private val storage = DatastoreUiStorage(
    dataStore = TestDatastoreFactory().datastore,
  )

  private val preferencesRepository = ProdPreferencesRepository(
    storage = storage,
    preferenceStorage = FakePreferenceStorage(),
  )

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ListDetailsViewModel(
      route = navArgs,
      preferencesRepository = preferencesRepository,
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

  fun mockListDetails(response: Resource<ListDetails?>) = apply {
    repository.mockFetchListDetails(response)
  }

  fun mockListDetails(response: Channel<Resource<ListDetails?>>) = apply {
    repository.mockFetchListDetails(response)
  }

  suspend fun updateSortBy(
    listId: Int,
    option: SortBy,
  ) = apply {
    preferencesRepository.switchListSortBy(listId, option)
  }

  suspend fun updateSortDirection(listId: Int) = apply {
    preferencesRepository.switchListSortDirection(listId)
  }

  suspend fun clearSortOption(listId: Int) = apply {
    preferencesRepository.clearListSortOption(listId)
  }

  suspend fun mockRemoveItems(response: Result<Int>) = apply {
    repository.mockRemoveItems(response)
  }
}
