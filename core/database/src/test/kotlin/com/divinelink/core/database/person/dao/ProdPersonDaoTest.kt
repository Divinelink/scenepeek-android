package com.divinelink.core.database.person.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdPersonDaoTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var database: Database

  private lateinit var dao: ProdPersonDao

  @BeforeTest
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()

    dao = ProdPersonDao(
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
}
