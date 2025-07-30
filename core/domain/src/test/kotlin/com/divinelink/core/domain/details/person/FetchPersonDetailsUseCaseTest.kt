package com.divinelink.core.domain.details.person

import app.cash.turbine.test
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory.toWzd
import com.divinelink.core.fixtures.model.person.credit.GroupedPersonCreditsSample
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.bruceAlmighty
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.despicableMe
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.littleMissSunshine
import com.divinelink.core.fixtures.model.person.credit.PersonCastCreditFactory.theOffice
import com.divinelink.core.fixtures.model.person.credit.PersonCombinedCreditsFactory
import com.divinelink.core.fixtures.model.person.credit.PersonCrewCreditFactory
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.KnownForDepartment
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestPersonRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

  private var params = PersonDetailsParams(
    id = 4495,
    knownForDepartment = "Acting",
  )

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

    useCase.invoke(params).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonDetails with failure person details response`() = runTest {
    repository.mockFetchPersonDetails(Result.failure(Exception("Something went wrong")))

    useCase.invoke(params).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonDetails with error`() = runTest {
    useCase.invoke(params).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      awaitComplete()
    }
  }

  @Test
  fun `test async fetchPersonDetails with failure person details response`() = runTest {
    repository.mockFetchPersonDetails(Result.failure(Exception("Something went wrong")))

    useCase.invoke(params.copy(knownForDepartment = null)).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      assertThat(awaitItem().exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      awaitComplete()
    }
  }

  @Test
  fun `test async fetchPersonDetails with error`() = runTest {
    useCase.invoke(params.copy(knownForDepartment = null)).test {
      val expectedFailure = awaitItem()
      assertThat(expectedFailure.exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      assertThat(expectedFailure.isFailure).isTrue()
      assertThat(awaitItem().exceptionOrNull()).isEqualTo(PersonDetailsResult.DetailsFailure)
      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonCredits for actors with success`() = runTest {
    repository.mockFetchPersonDetails(Result.success(PersonDetailsFactory.steveCarell()))
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(params).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
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
        PersonDetailsFactory.steveCarell()
          .copy(
            person = PersonDetailsFactory.steveCarell().person.copy(
              knownForDepartment = "Production",
            ),
          ),
      ),
    )
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(
      PersonDetailsParams(
        id = 4495,
        knownForDepartment = "Production",
      ),
    ).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(
          PersonDetailsFactory.steveCarell().copy(
            person = PersonDetailsFactory.steveCarell().person.copy(
              knownForDepartment = "Production",
            ),
          ),
        ),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCrewCreditFactory.productionSortedByPopularity(),
          knownForDepartment = "Production",
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchPersonCredits person without knownForDepartment data yields acting`() = runTest {
    repository.mockFetchPersonDetails(
      Result.success(
        PersonDetailsFactory.steveCarell().toWzd { withKnownForDepartment(null) },
      ),
    )
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    useCase.invoke(params).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(
          PersonDetailsFactory.steveCarell().toWzd { withKnownForDepartment(null) },
        ),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
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

    useCase.invoke(params).test {
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
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = mapOf(
            "Acting" to listOf(theOffice().copy(episodeCount = 0)),
            "Production" to listOf(PersonCrewCreditFactory.riot()),
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

    useCase.invoke(params).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchCredits awaits for person details iff knownForDepartment is null`() = runTest {
    val detailsChannel = Channel<Result<PersonDetails>>()
    repository.mockFetchPersonDetails(detailsChannel)
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    val params = PersonDetailsParams(
      id = 4495,
      knownForDepartment = null,
    )

    useCase.invoke(params).test {
      expectNoEvents()

      detailsChannel.send(Result.success(PersonDetailsFactory.steveCarell()))
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchCredits knownForDepartment defaults to Acting iff all are null`() = runTest {
    val detailsChannel = Channel<Result<PersonDetails>>()
    repository.mockFetchPersonDetails(detailsChannel)
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    val params = PersonDetailsParams(
      id = 4495,
      knownForDepartment = null,
    )

    useCase.invoke(params).test {
      expectNoEvents()

      detailsChannel.send(
        Result.success(PersonDetailsFactory.steveCarell().toWzd { withKnownForDepartment(null) }),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(
          PersonDetailsFactory.steveCarell().toWzd { withKnownForDepartment(null) },
        ),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test fetchCredits is concurrent iff knownForDepartment param is known`() = runTest {
    val detailsChannel = Channel<Result<PersonDetails>>()
    repository.mockFetchPersonDetails(detailsChannel)
    repository.mockFetchPersonCredits(Result.success(PersonCombinedCreditsFactory.all()))

    val paramss = PersonDetailsParams(
      id = 4495,
      knownForDepartment = "Acting",
    )

    useCase.invoke(paramss).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = GroupedPersonCreditsSample.movies(),
          tvShows = GroupedPersonCreditsSample.tvShows(),
        ),
      )

      delay(3000)
      detailsChannel.send(Result.success(PersonDetailsFactory.steveCarell()))

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      expectNoEvents()
    }
  }

  @Test
  fun `test fetchPersonCredits filters out departments without entries`() = runTest {
    repository.mockFetchPersonDetails(Result.success(PersonDetailsFactory.steveCarell()))
    repository.mockFetchPersonCredits(
      Result.success(
        PersonCombinedCreditsFactory.all()
          .copy(crew = listOf(PersonCrewCreditFactory.the40YearOldVirgin())),
      ),
    )

    useCase.invoke(params).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.DetailsSuccess(PersonDetailsFactory.steveCarell()),
      )

      assertThat(awaitItem().getOrNull()).isEqualTo(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = PersonCastCreditFactory.knownFor(),
          knownForDepartment = KnownForDepartment.Acting.value,
          movies = mapOf(
            "Acting" to listOf(
              bruceAlmighty(),
              littleMissSunshine(),
              despicableMe(),
            ),
            "Writing" to listOf(PersonCrewCreditFactory.the40YearOldVirgin()),
          ),
          tvShows = mapOf(
            "Acting" to listOf(theOffice()),
          ),
        ),
      )

      awaitComplete()
    }
  }
}
