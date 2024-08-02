package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.data.details.person.model.PersonDetailsResult
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchPersonDetailsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

class PersonViewModelTestRobot : ViewModelTestRobot<PersonUiState>() {

  private val fetchPersonDetailsUseCase = TestFetchPersonDetailsUseCase()

  private lateinit var viewModel: PersonViewModel
  private lateinit var navArgs: PersonNavArguments

  override val actualUiState: Flow<PersonUiState>
    get() = viewModel.uiState

  override fun buildViewModel() = apply {
    viewModel = PersonViewModel(
      fetchPersonDetailsUseCase = fetchPersonDetailsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
        ),
      ),
    )
  }

  fun withNavArgs(navArgs: PersonNavArguments) = apply {
    this.navArgs = navArgs
  }

  fun mockFetchPersonDetailsUseCaseSuccess(result: PersonDetailsResult) = apply {
    fetchPersonDetailsUseCase.mockSuccess(result)
  }

  fun setupChannelForUseCase(result: Channel<Result<PersonDetailsResult>>) = apply {
    fetchPersonDetailsUseCase.mockSuccess(result)
  }
}
