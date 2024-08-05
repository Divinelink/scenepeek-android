package com.divinelink.core.database.person.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.core.commons.ClockFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCastCreditEntityFactory
import com.divinelink.core.testing.factories.entity.person.credits.PersonCrewCreditEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdPersonDaoTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var clock: Clock
  private lateinit var database: Database
  private lateinit var dao: ProdPersonDao

  @BeforeTest
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()
    clock = ClockFactory.augustFirst2021()

    dao = ProdPersonDao(
      clock = clock,
      database = database,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test fetch person by id`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    dao.insertPerson(person)

    dao.fetchPersonById(id = person.id).test {
      assertThat(awaitItem()).isEqualTo(person)
      expectNoEvents()
    }
  }

  @Test
  fun `test inserting existing person replaces the person`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    dao.insertPerson(person)

    dao.fetchPersonById(id = person.id).test {
      val updatedPerson = person.copy(
        name = "Steve Carell Jr.",
      )

      dao.insertPerson(updatedPerson)
      assertThat(awaitItem()).isEqualTo(person)
      assertThat(awaitItem()).isEqualTo(updatedPerson)
      expectNoEvents()
    }
  }

  @Test
  fun `test fetch person credits by id`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    with(dao) {
      fetchPersonCredits(id = person.id).test {
        assertThat(awaitItem()).isNull()
        expectNoEvents()

        insertPersonCredits(id = person.id)

        val result = awaitItem()
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(person.id)
        assertThat(result?.insertedAt).isEqualTo(
          ClockFactory.augustFirst2021().currentEpochSeconds(),
        )
        expectNoEvents()
      }
    }
  }

  @Test
  fun `test insert and fetch person cast credits by id are sorted by date`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    val cast = PersonCastCreditEntityFactory.all()
    dao.insertPersonCastCredits(cast)

    with(dao) {
      fetchPersonCastCredits(id = person.id).test {
        assertThat(awaitItem()).isEqualTo(PersonCastCreditEntityFactory.sortedByDate())
        expectNoEvents()
      }
    }
  }

  @Test
  fun `test insert and fetch person top popular cast credits by id are sorted by date`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    val cast = PersonCastCreditEntityFactory.all()
    dao.insertPersonCastCredits(cast)

    with(dao) {
      fetchTopPopularCastCredits(id = person.id).test {
        assertThat(awaitItem()).isEqualTo(PersonCastCreditEntityFactory.sortedByPopularity())
        expectNoEvents()
      }
    }
  }

  @Test
  fun `test fetch person crew credits by id`() = runTest {
    val person = PersonEntityFactory.steveCarell()

    val crew = PersonCrewCreditEntityFactory.all()
    dao.insertPersonCrewCredits(crew)

    with(dao) {
      fetchPersonCrewCredits(id = person.id).test {
        assertThat(awaitItem()).isEqualTo(PersonCrewCreditEntityFactory.sortedByDate())
        expectNoEvents()
      }
    }
  }
}
