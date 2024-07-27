package com.divinelink.core.database.person.dao

import com.divinelink.core.database.Database
import com.divinelink.core.database.person.ProdPersonDao
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.entity.person.PersonEntityFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdPersonDaoTest {

  private lateinit var database: Database

  private lateinit var dao: ProdPersonDao

  @BeforeTest
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()

    dao = ProdPersonDao(database = database)
  }

  @Test
  fun `test fetch person by id`() {
    val person = PersonEntityFactory.steveCarell()

    dao.insertPerson(person)

    val result = dao.fetchPersonById(id = person.id)

    assertThat(result).isEqualTo(person)
  }
}
