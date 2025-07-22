package com.divinelink.core.data.list

import JvmUnitTestDemoAssetManager
import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.database.list.ProdListDao
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import com.divinelink.core.network.account.mapper.map
import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.list.model.CreateListRequest
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.dao.TestListDao
import com.divinelink.core.testing.database.TestDatabaseFactory
import com.divinelink.core.testing.service.TestListService
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdListRepositoryTest {

  private lateinit var repository: ListRepository

  private val service = TestListService()
  private val dao = TestListDao()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Before
  fun setUp() {
    repository = ProdListRepository(
      dao = dao.mock,
      service = service.mock,
    )

    repository = ProdListRepository(
      dao = dao.mock,
      service = service.mock,
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
  fun `test fetch list details with success`() = runTest {
    val listId = 123

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

    val result = repository.fetchListDetails(
      listId = listId,
      page = 1,
    )

    result.data shouldBe ListDetailsFactory.mustWatch()
  }

  @Test
  fun `test fetch user lists without local data and failure from api`() = runTest {
    dao.mockFetchUserLists(emptyList())
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
      dao = ProdListDao(
        database = TestDatabaseFactory.createInMemoryDatabase(),
        dispatcher = testDispatcher,
      ),
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
    val prodDao = ProdListDao(
      database = TestDatabaseFactory.createInMemoryDatabase(),
      dispatcher = testDispatcher,
    )
    repository = ProdListRepository(
      dao = prodDao,
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

    prodDao.insertListMetadata(
      accountId = "accountId",
      totalPages = ListItemFactory.page1().totalPages,
      totalResults = ListItemFactory.page1().totalResults,
    )

    prodDao.insertListItem(
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

    result.data.id shouldBe 123
    result.data.success shouldBe true
  }
}
