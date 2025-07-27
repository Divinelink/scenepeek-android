package com.divinelink.core.data.list

import JvmUnitTestDemoAssetManager
import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.database.list.ProdListDao
import com.divinelink.core.database.media.dao.ProdMediaDao
import com.divinelink.core.fixtures.core.commons.ClockFactory
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import com.divinelink.core.network.account.mapper.map
import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.create.CreateListRequest
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.dao.TestListDao
import com.divinelink.core.testing.dao.TestMediaDao
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.service.TestListService
import com.google.common.truth.Truth.assertThat
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdListRepositoryTest {

  private lateinit var repository: ListRepository

  private val sessionStorage = SessionStorageFactory.full()
  private val service = TestListService()
  private val listDao = TestListDao()
  private val mediaDao = TestMediaDao()
  private val clock: Clock = ClockFactory.decemberFirst2021()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Before
  fun setUp() {
    repository = ProdListRepository(
      listDao = listDao.mock,
      service = service.mock,
      sessionStorage = sessionStorage,
      mediaDao = mediaDao.mock,
      clock = clock,
    )
  }

  @Test
  fun `test add items to list when item already exists`() = runTest {
    val listId = 123

    val response = """
        {
          "success": true,
          "status_code": 1,
          "status_message": "Success.",
          "results": [
            {
              "media_id": 4108,
              "media_type": "tv",
              "error": [
                "Media has already been taken"
              ],
              "success": false
            }
          ]
        }
    """
      .trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    service.mockAddItemToList(
      Result.success(addToListResponse),
    )

    val result = repository.addItemToList(
      listId = listId,
      mediaId = 4108,
      mediaType = MediaType.TV.name,
    )

    result.data shouldBe AddToListResult.Failure.ItemAlreadyExists

    listDao.verifyNoInteraction()
  }

  @Test
  fun `test add items to list with success`() = runTest {
    val listId = 123

    val response = """
       {
         "success": true,
         "status_code": 1,
         "status_message": "Success.",
         "results": [
         {
           "media_id": 4108,
           "media_type": "tv",
           "success": true
         }
         ]
       }
    """.trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    service.mockAddItemToList(
      Result.success(addToListResponse),
    )

    val result = repository.addItemToList(
      listId = listId,
      mediaId = 4108,
      mediaType = MediaType.TV.name,
    )

    result.data shouldBe AddToListResult.Success
  }

  @Test
  fun `test add items to list also inserts media to database`() = runTest {
    val listId = 123

    val response = """
       {
         "success": true,
         "status_code": 1,
         "status_message": "Success.",
         "results": [
         {
           "media_id": 4108,
           "media_type": "tv",
           "success": true
         }
         ]
       }
    """.trimIndent()

    val addToListResponse = localJson.decodeFromString(AddToListResponse.serializer(), response)

    service.mockAddItemToList(
      Result.success(addToListResponse),
    )

    val result = repository.addItemToList(
      listId = listId,
      mediaId = 4108,
      mediaType = MediaType.TV.name,
    )

    result.data shouldBe AddToListResult.Success

    listDao.verifyMediaItemInserted(
      listId = listId,
      mediaId = 4108,
    )
  }

  @Test
  fun `test fetch list details with success and no cached data`() = runTest {
    val listId = 8542884

    val database = TestDatabaseFactory.createInMemoryDatabase()

    val dao = ProdListDao(
      database = database,
      dispatcher = testDispatcher,
    )
    val mediaDao = ProdMediaDao(
      database = database,
    )
    repository = ProdListRepository(
      listDao = dao,
      sessionStorage = sessionStorage,
      mediaDao = mediaDao,
      clock = clock,
      service = service.mock,
    )

    val response = JvmUnitTestDemoAssetManager
      .open("list-details.json")
      .use { inputStream ->
        val json = inputStream.readBytes().decodeToString().trimIndent()
        val serializer = ListDetailsResponse.serializer()
        localJson.decodeFromString(serializer, json)
      }

    service.mockFetchListDetails(
      Result.success(response),
    )

    repository.fetchListDetails(
      listId = listId,
      page = 1,
    ).test {
      awaitItem() shouldBeEqual Resource.Loading(null)

      assertThat(awaitItem()).isEqualTo(
        Resource.Success(ListDetailsFactory.mustWatch()),
      )
    }
  }

  @Test
  fun `test fetch user lists without local data and failure from api`() = runTest {
    listDao.mockFetchUserLists(emptyList())
    service.mockFetchUserListsFailure(RuntimeException("Network error"))

    repository.fetchUserLists(
      accountId = "accountId",
      page = 1,
    ).test {
      awaitItem() shouldBe Resource.Loading(null)
      awaitItem().toString() shouldBe Resource.Error(
        error = RuntimeException("Network error"),
        data = null,
      ).toString()

      awaitComplete()
    }
  }

  @Test
  fun `test fetch user lists without local data and success from api`() = runTest {
    val response = JvmUnitTestDemoAssetManager
      .open("lists.json")
      .use { inputStream ->
        val json = inputStream.readBytes().decodeToString().trimIndent()
        val serializer = ListsResponse.serializer()
        localJson.decodeFromString(serializer, json)
      }

    service.mockFetchUserLists(response)

    repository = ProdListRepository(
      listDao = ProdListDao(
        database = TestDatabaseFactory.createInMemoryDatabase(),
        dispatcher = testDispatcher,
      ),
      sessionStorage = sessionStorage,
      mediaDao = mediaDao.mock,
      clock = clock,
      service = service.mock,
    )

    repository.fetchUserLists(
      accountId = "accountId",
      page = 1,
    )
      .test {
        awaitItem() shouldBe Resource.Loading(null)

        awaitItem() shouldBe Resource.Success(
          data = PaginationData(
            page = 1,
            list = response.results.map { it.map() },
            totalPages = response.totalPages,
            totalResults = response.totalResults,
          ),
        )
      }
  }

  @Test
  fun `test fetch user lists with local data and success from api overrides local`() = runTest {
    val dao = ProdListDao(
      database = TestDatabaseFactory.createInMemoryDatabase(),
      dispatcher = testDispatcher,
    )
    repository = ProdListRepository(
      listDao = dao,
      sessionStorage = sessionStorage,
      mediaDao = mediaDao.mock,
      clock = clock,
      service = service.mock,
    )

    val response = JvmUnitTestDemoAssetManager
      .open("lists.json")
      .use { inputStream ->
        val json = inputStream.readBytes().decodeToString().trimIndent()
        val serializer = ListsResponse.serializer()
        localJson.decodeFromString(serializer, json)
      }

    service.mockFetchUserLists(response)

    dao.insertListMetadata(
      accountId = "accountId",
      totalPages = ListItemFactory.page1().totalPages,
      totalResults = ListItemFactory.page1().totalResults,
    )

    dao.insertListItem(
      page = 1,
      accountId = "accountId",
      items = ListItemFactory.page1().list,
    )

    repository.fetchUserLists(
      accountId = "accountId",
      page = 1,
    )
      .test {
        awaitItem() shouldBe Resource.Loading(
          ListItemFactory.page1(),
        )

        awaitItem() shouldBe Resource.Success(
          data = PaginationData(
            page = 1,
            list = response.results.map { it.map() },
            totalPages = response.totalPages,
            totalResults = response.totalResults,
          ),
        )
      }
  }

  @Test
  fun `test createList with success`() = runTest {
    val response = """
        {
          "id": 123,
          "status_message": "The item/record was created successfully.",
          "success": true,
          "status_code": 1
        }
    """.trimIndent()

    service.mockCreateList(
      Result.success(localJson.decodeFromString(response)),
    )

    val result = repository.createList(
      request = CreateListRequest.create(
        name = "Alfonso Bernard",
        description = "sit",
        public = true,
      ),
    )

    listDao.verifyInsertAtTheTopOfList(
      "B/52ok7mK0DqqG=pNBZ",
      ListItem(
        id = 123,
        name = "Alfonso Bernard",
        posterPath = null,
        backdropPath = null,
        description = "sit",
        public = true,
        numberOfItems = 0,
        updatedAt = "2021-12-01 08:00:00 UTC",
      ),
    )

    result.data.id shouldBe 123
    result.data.success shouldBe true
  }

  @Test
  fun `test deleteList with success also deleted from database`() = runTest {
    val listId = 123

    service.mockDeleteList(Result.success(Unit))

    repository.deleteList(listId)

    listDao.verifyListDeleted(listId)
  }

  @Test
  fun `test deleteList with failure `() = runTest {
    val listId = 123

    service.mockDeleteList(Result.failure(Exception("Foo")))

    repository.deleteList(listId)

    listDao.verifyNoInteraction()
  }

  @Test
  fun `test updateList with success also updates database`() = runTest {
    val request = UpdateListRequest.create(
      name = "Alfonso Bernard",
      description = "sit",
      public = true,
      backdrop = null,
    )
    service.mockUpdateList(
      id = 123,
      request = request,
      response = Result.success(
        localJson.decodeFromString(
          """
          {
            "success": true,
            "status_code": 1
          }
          """.trimIndent(),
        ),
      ),
    )

    repository.updateList(
      listId = 123,
      request = request,
    )

    listDao.verifyListUpdated(
      listId = 123,
      name = "Alfonso Bernard",
      description = "sit",
      isPublic = true,
      backdropPath = "",
    )
  }

  @Test
  fun `test updateList with success and backdrop update`() = runTest {
    val request = UpdateListRequest.create(
      name = "Alfonso Bernard",
      description = "sit",
      public = true,
      backdrop = "/path/to/backdrop.jpg",
    )
    service.mockUpdateList(
      id = 123,
      request = request,
      response = Result.success(
        localJson.decodeFromString(
          """
          {
            "success": true,
            "status_code": 1
          }
          """.trimIndent(),
        ),
      ),
    )

    repository.updateList(
      listId = 123,
      request = request,
    )

    listDao.verifyListUpdated(
      listId = 123,
      name = "Alfonso Bernard",
      description = "sit",
      isPublic = true,
      backdropPath = "/path/to/backdrop.jpg",
    )
  }

  @Test
  fun `test updateList when success is false`() = runTest {
    val request = UpdateListRequest.create(
      name = "Alfonso Bernard",
      description = "sit",
      public = true,
      backdrop = "/path/to/backdrop.jpg",
    )

    service.mockUpdateList(
      id = 123,
      request = request,
      response = Result.success(
        localJson.decodeFromString(
          """
          {
            "success": false,
            "status_code": 1
          }
          """.trimIndent(),
        ),
      ),
    )

    repository.updateList(
      listId = 123,
      request = request,
    )

    listDao.verifyNoInteraction()
  }

  @Test
  fun `test updateList with failure`() = runTest {
    val request = UpdateListRequest.create(
      name = "Alfonso Bernard",
      description = "sit",
      public = true,
      backdrop = "/path/to/backdrop.jpg",
    )

    service.mockUpdateList(
      id = 123,
      request = request,
      response = Result.failure(Exception("Update failed")),
    )

    repository.updateList(
      listId = 123,
      request = request,
    )

    listDao.verifyNoInteraction()
  }

  @Test
  fun `test fetchListsBackdrops with success`() = runTest {
    val listId = 123

    listDao.mockFetchListsBackdrops(
      listId = listId,
      backdrops = mapOf(
        "123" to "/path/to/backdrop1.jpg",
        "456" to "/path/to/backdrop2.jpg",
      ),
    )

    repository.fetchListsBackdrops(listId).test {
      awaitItem() shouldBeEqual mapOf(
        "123" to "/path/to/backdrop1.jpg",
        "456" to "/path/to/backdrop2.jpg",
      )

      awaitComplete()
    }
  }
}
