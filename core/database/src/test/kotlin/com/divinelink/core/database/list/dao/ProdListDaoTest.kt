package com.divinelink.core.database.list.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.database.list.ListMetadataEntity
import com.divinelink.core.database.list.ProdListDao
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.database.TestDatabaseFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdListDaoTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var database: Database

  private lateinit var dao: ProdListDao

  @Before
  fun setUp() {
    database = TestDatabaseFactory.createInMemoryDatabase()

    dao = ProdListDao(
      database = database,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test insert and fetch list items from index 0 and then 20`() = runTest {
    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 0,
    ).test {
      awaitItem() shouldBe emptyList()

      dao.insertListItem(
        page = 1,
        accountId = "one",
        items = ListItemFactory.page1Many().list,
      )

      awaitItem() shouldBe ListItemFactory.page1Many().list

      dao.insertListItem(
        page = 2,
        accountId = "one",
        items = ListItemFactory.page2Many().list,
      )

      awaitItem() shouldBe ListItemFactory.page1Many().list
    }

    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 20,
    ).test {
      awaitItem() shouldBe ListItemFactory.page2Many().list
    }
  }

  @Test
  fun `test re-inserting items in the same page clears stale data`() = runTest {
    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 0,
    ).test {
      awaitItem() shouldBe emptyList()

      dao.insertListItem(
        page = 1,
        accountId = "one",
        items = ListItemFactory.page1Many().list,
      )

      awaitItem() shouldBe ListItemFactory.page1Many().list

      dao.insertListItem(
        page = 1,
        accountId = "one",
        items = ListItemFactory.page1Many().list.take(10),
      )

      awaitItem() shouldBe ListItemFactory.page1Many().list.take(10)
    }
  }

  @Test
  fun `test insert and fetch list metadata`() = runTest {
    dao.fetchListsMetadata(accountId = "one") shouldBe null

    dao.insertListMetadata(
      accountId = "one",
      totalPages = 2,
      totalResults = 40,
    )

    dao.fetchListsMetadata(accountId = "one") shouldBe
      ListMetadataEntity(
        accountId = "one",
        totalPages = 2,
        totalResults = 40,
      )
  }

  @Test
  fun `test clear user lists`() = runTest {
    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 0,
    ).test {
      awaitItem() shouldBe emptyList()

      dao.insertListItem(
        page = 1,
        accountId = "one",
        items = ListItemFactory.page1Many().list,
      )

      awaitItem() shouldBe ListItemFactory.page1Many().list

      dao.clearUserLists(accountId = "one")

      awaitItem() shouldBe emptyList()
    }
  }

  @Test
  fun `test clear user lists with metadata`() = runTest {
    dao.fetchListsMetadata(accountId = "one") shouldBe null

    dao.insertListMetadata(
      accountId = "one",
      totalPages = 2,
      totalResults = 40,
    )

    dao.fetchListsMetadata(accountId = "one") shouldBe
      ListMetadataEntity(
        accountId = "one",
        totalPages = 2,
        totalResults = 40,
      )

    dao.clearUserLists(accountId = "one")

    dao.fetchListsMetadata(accountId = "one") shouldBe null
  }

  @Test
  fun `test clear user lists only clear specific account data`() = runTest {
    dao.insertListMetadata(
      accountId = "one",
      totalPages = 2,
      totalResults = 40,
    )

    dao.insertListMetadata(
      accountId = "two",
      totalPages = 1,
      totalResults = 20,
    )

    dao.insertListItem(
      page = 1,
      accountId = "one",
      items = ListItemFactory.page1Many().list,
    )

    dao.insertListItem(
      page = 1,
      accountId = "two",
      items = ListItemFactory.page2Many().list,
    )

    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 0,
    ).test {
      awaitItem() shouldBe ListItemFactory.page1Many().list
      dao.clearUserLists(accountId = "one")
      awaitItem() shouldBe emptyList()
    }

    dao.fetchUserLists(
      accountId = "two",
      fromIndex = 0,
    ).test {
      awaitItem() shouldBe ListItemFactory.page2Many().list
    }

    dao.fetchListsMetadata(accountId = "one") shouldBe null
    dao.fetchListsMetadata(accountId = "two") shouldBe
      ListMetadataEntity(
        accountId = "two",
        totalPages = 1,
        totalResults = 20,
      )
  }
}
