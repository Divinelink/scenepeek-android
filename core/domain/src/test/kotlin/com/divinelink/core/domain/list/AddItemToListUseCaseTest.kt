package com.divinelink.core.domain.list

import app.cash.turbine.test
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.ListException
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestListRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class AddItemToListUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val repository = TestListRepository()

  @Test
  fun `test add item to list with success`() = runTest {
    val useCase = AddItemToListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockAddItemToList(
      Result.success(AddToListResult.Success),
    )

    useCase.invoke(
      AddItemParameters(
        listId = 1234,
        media = MediaReference(
          mediaId = 3456,
          mediaType = MediaType.MOVIE,
        ),
      ),
    ).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(true),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test add item to list when item already exists`() = runTest {
    val useCase = AddItemToListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockAddItemToList(
      Result.success(AddToListResult.Failure.ItemAlreadyExists),
    )

    useCase.invoke(
      AddItemParameters(
        listId = 1234,
        media = MediaReference(
          mediaId = 3456,
          mediaType = MediaType.MOVIE,
        ),
      ),
    ).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(ListException.ItemAlreadyExists()).toString(),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test add item to list with unexpected error`() = runTest {
    val useCase = AddItemToListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockAddItemToList(
      Result.success(AddToListResult.Failure.UnexpectedError),
    )

    useCase.invoke(
      AddItemParameters(
        listId = 1234,
        media = MediaReference(
          mediaId = 3456,
          mediaType = MediaType.MOVIE,
        ),
      ),
    ).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(ListException.UnexpectedError()).toString(),
      )

      awaitComplete()
    }
  }

  @Test
  fun `test add item to list with failure`() = runTest {
    val useCase = AddItemToListUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    repository.mockAddItemToList(
      Result.failure(Exception("Foo exception")),
    )

    useCase.invoke(
      AddItemParameters(
        listId = 1234,
        media = MediaReference(
          mediaId = 3456,
          mediaType = MediaType.MOVIE,
        ),
      ),
    ).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(Exception("Foo exception")).toString(),
      )

      awaitComplete()
    }
  }
}
