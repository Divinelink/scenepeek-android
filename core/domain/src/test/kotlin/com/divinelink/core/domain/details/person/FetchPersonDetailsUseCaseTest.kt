package com.divinelink.core.domain.details.person

import app.cash.turbine.test
import com.divinelink.core.data.details.person.model.PersonDetailsResult
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
import com.divinelink.core.testing.repository.TestPersonRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class FetchPersonDetailsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestPersonRepository
  private lateinit var useCase: FetchPersonDetailsUseCase

  @BeforeTest
  fun setUp() {
    repository = TestPersonRepository()

    useCase = FetchPersonDetailsUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test fetchPersonDetails with success person details response`() = runTest {
    repository.mockFetchPersonDetails(Result.success(PersonDetailsFactory.steveCarell()))

    useCase.invoke(4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonDetails with failure person details response`() = runTest {
    repository.mockFetchPersonDetails(Result.failure(Exception("Something went wrong")))

    useCase.invoke(4495).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonDetails with error`() = runTest {
    useCase.invoke(4495).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      awaitComplete()
    }
  }
}
