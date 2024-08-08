package com.divinelink.core.domain.details.person

import app.cash.turbine.test
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory.bruceAlmighty
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory.despicableMe
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory.littleMissSunshine
import com.divinelink.core.testing.factories.model.person.credit.PersonCastCreditFactory.theOffice
import com.divinelink.core.testing.factories.model.person.credit.PersonCombinedCreditsFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCrewCreditFactory
import com.divinelink.core.testing.repository.TestPersonRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
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
      assertThat(awaitItem().exceptionOrNull()?.cause).isEqualTo(PersonDetailsResult.DetailsFailure)
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonDetails with error`() = runTest {
    useCase.invoke(4495).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      assertThat(awaitItem().exceptionOrNull()?.cause).isEqualTo(PersonDetailsResult.DetailsFailure)
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonCredits for actors with success`() = runTest {
    repository.mockFetchPersonDetails(Result.success(PersonDetailsFactory.steveCarell()))
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          credits = PersonCombinedCreditsFactory.all(),
        ),
      )

      awaitComplete()
    }
  }

  // Known for department is for crew are sorted by popularity.
  @Test
  fun `test fetchPersonCredits for crew returns crew credits on known for dep`() = runTest {
    repository.mockFetchPersonDetails(
      Result.success(
        PersonDetailsFactory.steveCarell().copy(knownForDepartment = "Production"),
      ),
    )
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(
          PersonDetailsFactory.steveCarell().copy(knownForDepartment = "Production"),
        ),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCrewCreditFactory.productionSortedByPopularity(),
          credits = PersonCombinedCreditsFactory.all(),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonCredits person without knownForDepartment data yields acting`() = runTest {
    repository.mockFetchPersonDetails(
      Result.success(
        PersonDetailsFactory.steveCarell().copy(knownForDepartment = null),
      ),
    )
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(
          PersonDetailsFactory.steveCarell().copy(knownForDepartment = null),
        ),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          credits = PersonCombinedCreditsFactory.all(),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test knownFor section does not include tv shows without episode count`() = runTest {
    repository.mockFetchPersonDetails(Result.success(PersonDetailsFactory.steveCarell()))
    repository.mockFetchPersonCredits(
      Result.success(
        PersonCombinedCreditsFactory.all().copy(
          cast = listOf(
            bruceAlmighty(),
            littleMissSunshine(),
            despicableMe(),
            theOffice().copy(episodeCount = 0),
          ),
        ),
      ),
    )

    useCase.invoke(4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = listOf(
            // This should be excluded but we'll keep it for now.
            theOffice().copy(episodeCount = 0),
            littleMissSunshine(),
            despicableMe(),
            bruceAlmighty(),
          ),
          credits = PersonCombinedCreditsFactory.all().copy(
            cast = listOf(
              bruceAlmighty(),
              littleMissSunshine(),
              despicableMe(),
              theOffice().copy(episodeCount = 0),
            ),
          ),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonCredits with failure`() = runTest {
    repository.mockFetchPersonDetails(Result.success(PersonDetailsFactory.steveCarell()))
    repository.mockFetchPersonCredits(Result.failure(Exception("Something went wrong")))

    useCase.invoke(4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchCredits awaits for person details`() = runTest {
    val detailsChannel = Channel<Result<PersonDetails>>()
    repository.mockFetchPersonDetails(detailsChannel)
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(4495).test {
      expectNoEvents()

      detailsChannel.send(Result.success(PersonDetailsFactory.steveCarell()))
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          credits = PersonCombinedCreditsFactory.all(),
        ),
      )

      awaitComplete()
    }
  }
}
