package com.divinelink.core.domain

import com.divinelink.core.domain.change.FetchChangesUseCase
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.model.change.StringValue
import com.divinelink.core.model.person.Gender
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.change.ChangeSample
import com.divinelink.core.testing.factories.model.change.PersonChangesSample
import com.divinelink.core.testing.repository.TestPersonRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class FetchChangesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestPersonRepository

  @BeforeTest
  fun setUp() {
    repository = TestPersonRepository()
  }

  @Test
  fun `test fetch changes when date range only includes today's date`() = runTest {
    repository.mockFetchPersonDetails(
      Result.success(PersonDetailsFactory.steveCarell()),
    )

    val useCase = FetchChangesUseCase(
      repository = repository.mock,
      clock = ClockFactory.augustFifteenth2021(),
      dispatcher = testDispatcher,
    )

    useCase.invoke(4495)

    repository.verifyNoUpdatesForPerson()
  }

  @Test
  fun `test fetch changes with multiple 14 day periods`() = runTest {
    repository.mockFetchPersonDetails(
      Result.success(PersonDetailsFactory.steveCarell()),
    )

    repository.mockFetchChanges(
      Result.success(ChangeSample.changes()),
    )

    val useCase = FetchChangesUseCase(
      repository = repository.mock,
      clock = ClockFactory.decemberFirst2021(),
      dispatcher = testDispatcher,
    )

    useCase.invoke(4495)

    repository.verifyExecuteFetchChanges(invocations = 8)
  }

  @Test
  fun `test fetch changes with data correctly updates database`() = runTest {
    repository.mockFetchPersonDetails(
      Result.success(PersonDetailsFactory.steveCarell()),
    )

    repository.mockFetchChanges(
      Result.success(ChangeSample.changes()),
    )

    val useCase = FetchChangesUseCase(
      repository = repository.mock,
      clock = ClockFactory.augustTwentyEighth2021(),
      dispatcher = testDispatcher,
    )

    useCase.invoke(4495)

    repository.verifyExecuteFetchChanges()

    repository.verifyUpdatePerson(id = 4495, imdbId = "nm8874716")
    repository.verifyUpdatePerson(id = 4495, birthday = "1986-12-04")
    repository.verifyUpdatePerson(id = 4495, placeOfBirth = "Aridaia, Central Macedonia, Greece")
    repository.verifyUpdatePerson(
      id = 4495,
      biography = (PersonChangesSample.biography().last().value as StringValue).value,
    )
    repository.verifyUpdatePerson(id = 4495, gender = Gender.MALE.value)

    repository.verifyUpdatePerson(
      id = 4495,
      insertedAt = ClockFactory.augustTwentyEighth2021().now().epochSeconds.toString(),
    )
  }

  @Test
  fun `test fetchChanges without person details does not trigger calls`() = runTest {
    repository.mockFetchPersonDetails(Result.failure(Exception()))

    val useCase = FetchChangesUseCase(
      repository = repository.mock,
      clock = ClockFactory.augustFifteenth2021(),
      dispatcher = testDispatcher,
    )

    useCase.invoke(4495)

    repository.verifyNoUpdatesForPerson()
  }
}
