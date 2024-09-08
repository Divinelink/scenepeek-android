package com.divinelink.feature.details.person.ui

import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchChangesUseCase
import com.divinelink.core.testing.usecase.TestFetchPersonDetailsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

class PersonViewModelTestRobot : ViewModelTestRobot<PersonUiState>() {

  private val fetchPersonDetailsUseCase = TestFetchPersonDetailsUseCase()
  private val fetchChangesUseCase = TestFetchChangesUseCase()

  private lateinit var viewModel: PersonViewModel
  private lateinit var navArgs: PersonNavArguments

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

  fun withNavArgs(navArgs: PersonNavArguments) = apply {
    this.navArgs = navArgs
  }

  fun mockFetchPersonDetailsUseCaseSuccess(result: PersonDetailsResult) = apply {
    fetchPersonDetailsUseCase.mockSuccess(result)
  }

  fun setupChannelForUseCase(result: Channel<Result<PersonDetailsResult>>) = apply {
    fetchPersonDetailsUseCase.mockSuccess(result)
  }

  // Essentially, when this is success the dao should emit new values for a person
  suspend fun mockFetchChangesUseCaseSuccess() = apply {
    fetchChangesUseCase.mockSuccess()
  }
}
