package com.divinelink.core.database.media.dao

import com.divinelink.core.database.Database
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.testing.database.TestDatabaseFactory
import io.kotest.matchers.shouldBe
import org.junit.Before
import kotlin.test.Test

class ProdMediaDaoTest {

  private lateinit var database: Database
  private lateinit var dao: ProdMediaDao

  @Before
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()

    dao = ProdMediaDao(
      database = database,
    )
  }

  @Test
  fun `test insert and fetch media items`() {
    val item = dao.fetchMediaItemById(MediaItemFactory.theWire().id)

    item shouldBe null

    dao.insertMedia(MediaItemFactory.theWire())

    val fetchedItem = dao.fetchMediaItemById(MediaItemFactory.theWire().id)

    fetchedItem shouldBe MediaItemFactory.theWire()
  }

  @Test
  fun `test insert list of items`() {
    dao.fetchMediaItemById(MediaItemFactory.theWire().id) shouldBe null
    dao.fetchMediaItemById(MediaItemFactory.theOffice().id) shouldBe null

    dao.insertMedia(MediaItemFactory.tvAll())

    dao.fetchMediaItemById(MediaItemFactory.theWire().id) shouldBe MediaItemFactory.theWire()
    dao.fetchMediaItemById(MediaItemFactory.theOffice().id) shouldBe MediaItemFactory.theOffice()
  }
}
