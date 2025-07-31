package com.divinelink.core.database.media.dao

import com.divinelink.core.database.Database
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.toStub
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
    val item = dao.fetchMedia(MediaItemFactory.theWire().toStub())

    item shouldBe null

    dao.insertMedia(MediaItemFactory.theWire())

    val fetchedItem = dao.fetchMedia(MediaItemFactory.theWire().toStub())

    fetchedItem shouldBe MediaItemFactory.theWire()
  }

  @Test
  fun `test insert list of items`() {
    dao.fetchMedia(MediaItemFactory.theWire().toStub()) shouldBe null
    dao.fetchMedia(MediaItemFactory.theOffice().toStub()) shouldBe null

    dao.insertMedia(MediaItemFactory.tvAll())

    dao.fetchMedia(MediaItemFactory.theWire().toStub()) shouldBe MediaItemFactory.theWire()
    dao.fetchMedia(MediaItemFactory.theOffice().toStub()) shouldBe MediaItemFactory.theOffice()
  }
}
