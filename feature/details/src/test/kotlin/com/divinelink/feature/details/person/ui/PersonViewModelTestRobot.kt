package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchChangesUseCase
import com.divinelink.core.testing.usecase.TestFetchPersonDetailsUseCase
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

class PersonViewModelTestRobot : ViewModelTestRobot<PersonUiState>() {

  private val fetchPersonDetailsUseCase = TestFetchPersonDetailsUseCase()
  private val fetchChangesUseCase = TestFetchChangesUseCase()

  private lateinit var viewModel: PersonViewModel
  private lateinit var navArgs: PersonRoute

  override val actualUiState: Flow<PersonUiState>
    get() = viewModel.uiState

  override fun buildViewModel() = apply {
    viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      fetchChangesUseCase = fetchChangesUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "knownForDepartment" to navArgs.knownForDepartment,
          "name" to navArgs.name,
          "profilePath" to navArgs.profilePath,
          "gender" to navArgs.gender,
        ),
      ),
    )
  }

  fun withNavArgs(navArgs: PersonRoute) = apply {
    this.navArgs = navArgs
  }

  fun mockFetchPersonDetailsUseCaseSuccess(result: PersonDetailsResult) = apply {
    fetchPersonDetailsUseCase.mockSuccess(result)
  }

  fun setupChannelForUseCase(result: Channel<Result<PersonDetailsResult>>) = apply {
    fetchPersonDetailsUseCase.mockSuccess(result)
  }

  fun onTabSelected(tab: Tab) = apply {
    viewModel.onTabSelected(tab.order)
  }

  fun onApplyFilter(filter: CreditFilter) = apply {
    viewModel.onApplyFilter(filter)
  }

  // Essentially, when this is success the dao should emit new values for a person
  suspend fun mockFetchChangesUseCaseSuccess() = apply {
    fetchChangesUseCase.mockSuccess()
  }
}
