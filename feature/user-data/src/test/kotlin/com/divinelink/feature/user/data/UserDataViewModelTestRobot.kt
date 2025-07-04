package com.divinelink.feature.user.data

import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.UserDataRoute
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchUserDataUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import kotlinx.coroutines.flow.Flow

class UserDataViewModelTestRobot : ViewModelTestRobot<UserDataUiState>() {

  private lateinit var viewModel: UserDataViewModel
  private lateinit var navArgs: UserDataRoute

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchUserDataUseCase = TestFetchUserDataUseCase()

  fun withSection(section: UserDataSection) = apply {
    this.navArgs = UserDataRoute(section)
  }

  override fun buildViewModel() = apply {
    viewModel = UserDataViewModel(
      observeAccountUseCase = observeAccountUseCase.mock,
      fetchUserDataUseCase = fetchUserDataUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "userDataSection" to navArgs.userDataSection,
        ),
      ),
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
}
