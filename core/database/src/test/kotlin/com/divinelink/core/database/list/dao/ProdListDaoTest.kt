package com.divinelink.core.database.list.dao

import app.cash.turbine.test
import com.divinelink.core.database.Database
import com.divinelink.core.database.list.ListMetadataEntity
import com.divinelink.core.database.list.ProdListDao
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
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

  @Test
  fun `test insertAtTopOfList inserts item at the top`() = runTest {
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

      dao.insertAtTheTopOfList(
        accountId = "one",
        item = ListItemFactory.shows(),
      )

      awaitItem() shouldBe listOf(
        ListItemFactory.shows(),
      ) + ListItemFactory.page1Many().list.take(19)

      dao.insertAtTheTopOfList(
        accountId = "one",
        item = ListItemFactory.movies(),
      )

      awaitItem() shouldBe listOf(
        ListItemFactory.movies(),
        ListItemFactory.shows(),
      ) + ListItemFactory.page1Many().list.take(18)
    }

    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 20,
    ).test {
      awaitItem() shouldBe ListItemFactory.page1Many().list.takeLast(2)
    }
  }

  @Test
  fun `test fetchListDetails without inserted media returns details with empty list`() = runTest {
    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch().copy(media = emptyList()),
      )

      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(media = emptyList())
    }
  }

  @Test
  fun `test fetchListDetails does not return items if they are not inserted to db`() = runTest {
    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch(),
      )

      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(media = emptyList())
    }
  }

  @Test
  fun `test fetchListDetails returns media list when media items exist in db`() = runTest {
    ListDetailsFactory.mustWatch().media.forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch(),
      )

      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(
        media = emptyList(),
      )
      awaitItem() shouldBe ListDetailsFactory.mustWatch()
    }
  }

  @Test
  fun `test insertMediaToList inserts media item to list updates listItem count`() = runTest {
    ListDetailsFactory.mustWatch().media.forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    dao.fetchUserLists(
      accountId = "one",
      fromIndex = 0,
    ).test {
      awaitItem() shouldBe emptyList()

      dao.insertListItem(
        page = 1,
        accountId = "one",
        items = ListItemFactory.page1().list,
      )

      awaitItem() shouldBe ListItemFactory.page1().list

      dao.insertMediaToList(
        listId = ListDetailsFactory.mustWatch().id,
        mediaId = ListDetailsFactory.mustWatch().media.first().id,
      )

      awaitItem() shouldBe ListItemFactory.page1().list.map { item ->
        if (item.id == ListDetailsFactory.mustWatch().id) {
          item.copy(numberOfItems = item.numberOfItems + 1)
        } else {
          item
        }
      }

      // When item already exists, it should not change the count
      dao.insertMediaToList(
        listId = ListDetailsFactory.mustWatch().id,
        mediaId = ListDetailsFactory.mustWatch().media.first().id,
      )

      expectNoEvents()
    }
  }

  @Test
  fun `test insertMediaToList inserts media item to bottom of list`() = runTest {
    ListDetailsFactory.mustWatch().media.forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch().copy(
          media = listOf(
            MediaItemFactory.theWire(),
            MediaItemFactory.FightClub(),
          ),
        ),
      )

      awaitItem()
      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(
        media = listOf(
          MediaItemFactory.theWire(),
          MediaItemFactory.FightClub(),
        ),
      )

      dao.insertMediaToList(
        listId = ListDetailsFactory.mustWatch().id,
        mediaId = MediaItemFactory.theOffice().id,
      )

      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(
        media = listOf(
          MediaItemFactory.theWire(),
          MediaItemFactory.FightClub(),
          MediaItemFactory.theOffice(),
        ),
      )
    }
  }

  @Test
  fun `test deleteList removes list and its items`() = runTest {
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

      val listId = ListDetailsFactory.mustWatch().id
      dao.deleteList(listId)

      awaitItem() shouldBe ListItemFactory.page1Many().list.filterNot { it.id == listId }
    }
  }

  @Test
  fun `test updateList updates list details`() = runTest {
    ListDetailsFactory.mustWatch().media.forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch(),
      )

      awaitItem()
      awaitItem() shouldBe ListDetailsFactory.mustWatch()

      val updatedName = "Updated Must Watch"
      dao.updateList(
        listId = ListDetailsFactory.mustWatch().id,
        name = updatedName,
        description = "Updated description",
        backdropPath = "/newBackdrop.jpg",
        isPublic = true,
      )

      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(
        name = updatedName,
        description = "Updated description",
        backdropPath = "/newBackdrop.jpg",
        public = true,
      )

      dao.updateList(
        listId = ListDetailsFactory.mustWatch().id,
        name = "Another Update",
        description = "Another description",
        backdropPath = "/anotherBackdrop.jpg",
        isPublic = false,
      )

      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(
        name = "Another Update",
        description = "Another description",
        backdropPath = "/anotherBackdrop.jpg",
        public = false,
      )
    }
  }

  @Test
  fun `test fetch list backdrops returns empty map when no items exist`() = runTest {
    dao.fetchListsBackdrops(
      listId = ListDetailsFactory.mustWatch().id,
    ).test {
      awaitItem() shouldBe emptyMap()

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch(),
      )

      awaitItem() shouldBe emptyMap()
    }
  }

  @Test
  fun `test fetch list backdrops returns map of backdrops when items exist`() = runTest {
    ListDetailsFactory.mustWatch().media.forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch(),
      )

      awaitItem()
      awaitItem() shouldBe ListDetailsFactory.mustWatch()

      dao.fetchListsBackdrops(
        listId = ListDetailsFactory.mustWatch().id,
      ).test {
        awaitItem() shouldBe mapOf(
          MediaItemFactory.theWire().name to MediaItemFactory.theWire().backdropPath,
          MediaItemFactory.FightClub().name to MediaItemFactory.FightClub().backdropPath,
          MediaItemFactory.theOffice().name to MediaItemFactory.theOffice().backdropPath,
        )
      }
    }
  }

  @Test
  fun `test fetch list backdrops with null or empty backdrop and names`() = runTest {
    listOf(
      MediaItemFactory.theWire().copy(name = ""),
      MediaItemFactory.FightClub().copy(backdropPath = null),
      MediaItemFactory.theOffice(),
    ).forEach {
      database.mediaItemEntityQueries.insertMediaItem(it.map())
    }

    dao.fetchListDetails(
      listId = ListDetailsFactory.mustWatch().id,
      page = 1,
    ).test {
      awaitItem() shouldBe null

      dao.insertListDetails(
        page = 1,
        details = ListDetailsFactory.mustWatch(),
      )

      awaitItem()
      awaitItem() shouldBe ListDetailsFactory.mustWatch().copy(
        media = listOf(
          MediaItemFactory.theWire().copy(name = ""),
          MediaItemFactory.FightClub().copy(backdropPath = null),
          MediaItemFactory.theOffice(),
        ),
      )

      dao.fetchListsBackdrops(
        listId = ListDetailsFactory.mustWatch().id,
      ).test {
        awaitItem() shouldBe mapOf(
          MediaItemFactory.theOffice().name to MediaItemFactory.theOffice().backdropPath,
        )
      }
    }
  }

  @Test
  fun `test clear user lists with no items does not throw error`() = runTest {
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
}
