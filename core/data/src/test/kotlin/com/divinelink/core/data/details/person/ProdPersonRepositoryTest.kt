package com.divinelink.core.data.details.person

import JvmUnitTestDemoAssetManager
import app.cash.turbine.test
import com.divinelink.core.data.person.details.mapper.map
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.details.person.PersonDetailsApiFactory
import com.divinelink.core.testing.factories.core.commons.ClockFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCastCreditEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCrewCreditEntityFactory
import com.divinelink.core.testing.factories.model.person.credit.PersonCombinedCreditsFactory
import com.divinelink.core.testing.service.TestPersonService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdPersonRepositoryTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: ProdPersonRepository
  private lateinit var service: TestPersonService
  private lateinit var dao: PersonDao
  private lateinit var clock: Clock

  @BeforeTest
  fun setUp() {
    dao = ProdPersonDao(
      clock = ClockFactory.augustFifteenth2021(),
      database = TestDatabaseFactory.createInMemoryDatabase(),
      dispatcher = testDispatcher,
    )
    service = TestPersonService()
    clock = ClockFactory.augustFifteenth2021()

    repository = ProdPersonRepository(
      service = service.mock,
      dao = dao,
      clock = clock,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test fetchPersonDetails with local data only fetches from database`() = runTest {
    dao.insertPerson(PersonEntityFactory.steveCarell())

    repository.fetchPersonDetails(id = 4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(PersonEntityFactory.steveCarell().map())
      expectNoEvents()
    }
  }

  @Test
  fun `test fetchPersonDetails without cache fetches from service and saves to db`() = runTest {
    service.mockFetchPersonDetails(response = PersonDetailsApiFactory.steveCarell())

    repository.fetchPersonDetails(id = 4495).test {
      assertThat(awaitItem().getOrNull()).isEqualTo(PersonEntityFactory.steveCarell().map())
      expectNoEvents()
    }
  }

  @Test
  fun `test fetchPersonCredits with local data only fetches from database`() = runTest {
    dao.insertPersonCredits(id = 4495)
    dao.insertPersonCastCredits(cast = PersonCastCreditEntityFactory.all())
    dao.insertPersonCrewCredits(crew = PersonCrewCreditEntityFactory.all())

    repository.fetchPersonCredits(id = 4495).test {
      val cached = awaitItem().getOrNull()
      assertThat(cached).isEqualTo(PersonCombinedCreditsFactory.sortedByDate())
      expectNoEvents()
    }
  }

  @Test
  fun `test fetchPersonCredits without cache fetches from service and saves to db`() = runTest {
    JvmUnitTestDemoAssetManager.open("person-combined-credits.json").use {
      val personDetails = it.readBytes().decodeToString().trimIndent()

      val serializer = PersonCreditsApi.serializer()
      val personCreditsApi = localJson.decodeFromString(serializer, personDetails)

      service.mockFetchPersonCombinedCredits(response = personCreditsApi)

      repository.fetchPersonCredits(id = 4495).test {
        val first = awaitItem().getOrNull()
        assertThat(first?.cast?.size).isEqualTo(124)
        assertThat(first?.crew?.size).isEqualTo(17)
        awaitItem()
        expectNoEvents()
      }
    }
  }
}
