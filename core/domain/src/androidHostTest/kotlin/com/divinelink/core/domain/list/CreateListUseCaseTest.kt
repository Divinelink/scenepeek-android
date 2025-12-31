package com.divinelink.core.domain.list

import app.cash.turbine.test
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestListRepository
import io.kotest.matchers.shouldBe
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
  fun `test create list with success`() = runTest {
    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockCreateList(
      Result.success(createListResult),
    )

    useCase.invoke(
      parameters,
    ).test {
      awaitItem() shouldBe Result.success(createListResult.id)
      awaitComplete()
    }
  }

  @Test
  fun `test create list with error exception`() = runTest {
    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
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
    val useCase = CreateListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
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
