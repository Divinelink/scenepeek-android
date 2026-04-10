package com.divinelink.feature.user.data

import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.Navigation.UserDataRoute
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchUserDataUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import kotlinx.coroutines.flow.Flow

class UserDataViewModelTestRobot : ViewModelTestRobot<UserDataUiState>() {

  private lateinit var viewModel: UserDataViewModel
  private lateinit var navArgs: UserDataRoute

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchUserDataUseCase = TestFetchUserDataUseCase()
  private val preferencesRepository = TestPreferencesRepository()

  fun withSection(section: UserDataSection) = apply {
    this.navArgs = UserDataRoute(section)
  }

  override fun buildViewModel() = apply {
    viewModel = UserDataViewModel(
      route = navArgs,
      preferencesRepository = preferencesRepository,
      observeAccountUseCase = observeAccountUseCase.mock,
      fetchUserDataUseCase = fetchUserDataUseCase.mock,
    )
  }

  override val actualUiState: Flow<UserDataUiState>
    get() = viewModel.uiState

  fun mockObserveAccount(response: TestObserveAccountUseCase.() -> Unit) = apply {
    observeAccountUseCase.response()
  }

  fun mockFetchUserData(response: TestFetchUserDataUseCase.() -> Unit) = apply {
    fetchUserDataUseCase.response()
  }

  fun onLoadMore() = apply {
    viewModel.onLoadMore()
  }

  fun selectTab(tabIndex: Int) = apply {
    viewModel.onTabSelected(tabIndex)
  }

  fun onRefresh() = apply {
    viewModel.onRefresh()
  }

  suspend fun switchSortDirection() = apply {
    preferencesRepository.switchSortDirection(section = ViewableSection.USER_DATA)
  }
}
