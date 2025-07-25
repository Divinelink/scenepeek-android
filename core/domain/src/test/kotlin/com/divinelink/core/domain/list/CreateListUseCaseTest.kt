package com.divinelink.core.domain.list

import app.cash.turbine.test
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestListRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class CreateListUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val repository = TestListRepository()

  private val parameters = CreateListParameters(
    name = "My New List",
    description = "This is a test list",
    public = true,
  )

  private val createListResult = CreateListResult(
    id = 1,
    success = true,
  )

  @Test
  fun `test create list without accountId`() = runTest {
    val sessionStorage = SessionStorageFactory.noAccountId()

    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
    )

    repository.mockCreateList(
      Result.success(createListResult),
    )

    useCase.invoke(
      parameters,
    ).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      awaitComplete()
    }
  }

  @Test
  fun `test create list with success`() = runTest {
    val sessionStorage = SessionStorageFactory.full()

    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
    )

    repository.mockCreateList(
      Result.success(createListResult),
    )

    repository.mockFetchUserLists(
      flowOf(
        Resource.Loading(),
        Resource.Success(data = null),
      ),
    )

    useCase.invoke(
      parameters,
    ).test {
      awaitItem() shouldBe Result.success(createListResult.id)
      awaitItem() shouldBe Result.success(createListResult.id)

      awaitComplete()
    }
  }

  @Test
  fun `test create list with success but fetch failure`() = runTest {
    val sessionStorage = SessionStorageFactory.full()

    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
    )

    repository.mockCreateList(
      Result.success(createListResult),
    )

    repository.mockFetchUserLists(
      flowOf(
        Resource.Error(
          error = Exception("Foo"),
        ),
      ),
    )

    useCase.invoke(
      parameters,
    ).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        Exception("Failed to fetch user lists"),
      ).toString()

      awaitComplete()
    }
  }

  @Test
  fun `test create list with error exception`() = runTest {
    val sessionStorage = SessionStorageFactory.full()

    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
    )

    repository.mockCreateList(
      Result.failure(Exception("Foo")),
    )

    useCase.invoke(
      parameters,
    ).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        Exception("Foo"),
      ).toString()

      awaitComplete()
    }
  }

  @Test
  fun `test create list with success false`() = runTest {
    val sessionStorage = SessionStorageFactory.full()

    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
      sessionStorage = sessionStorage,
    )

    repository.mockCreateList(
      Result.success(
        createListResult.copy(success = false),
      ),
    )

    useCase.invoke(
      parameters,
    ).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        Exception("Failed to create list"),
      ).toString()

      awaitComplete()
    }
  }
}
