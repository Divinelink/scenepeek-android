package com.divinelink.core.data.details.person

import app.cash.turbine.test
import com.divinelink.core.data.person.details.mapper.map
import com.divinelink.core.data.person.repository.ProdPersonRepository
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.api.details.person.PersonDetailsApiFactory
import com.divinelink.core.testing.factories.core.commons.ClockFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
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
}
