package com.divinelink.core.domain

import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetDetailsActionItemsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test detail action items without jellyseerr account`() = runTest {
    val useCase = createGetDetailsActionItemsUseCase(FakePreferenceStorage())

    useCase.invoke(Unit).first().let { result ->
      assertThat(result.isSuccess).isTrue()
      assertThat(result.getOrNull()).isEqualTo(
        listOf(
          DetailActionItem.Rate,
          DetailActionItem.Watchlist,
          DetailActionItem.List,
        ),
      )
    }
  }

  @Test
  fun `test detail action items with jellyseerr account`() = runTest {
    val useCase = createGetDetailsActionItemsUseCase(
      FakePreferenceStorage(jellyseerrAccount = "jellyseerr_account"),
    )

    useCase.invoke(Unit).first().let { result ->
      assertThat(result.isSuccess).isTrue()
      assertThat(result.getOrNull()).isEqualTo(
        listOf(
          DetailActionItem.Rate,
          DetailActionItem.Watchlist,
          DetailActionItem.List,
          DetailActionItem.Request,
        ),
      )
    }
  }

  private fun createGetDetailsActionItemsUseCase(storage: FakePreferenceStorage) =
    GetDetailsActionItemsUseCase(
      storage = storage,
      dispatcher = testDispatcher,
    )
}
