package com.divinelink.core.domain

import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.testing.MainDispatcherRule
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
  fun `test detail action items`() = runTest {
    val useCase = createUseCase()

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

  private fun createUseCase() = GetDetailsActionItemsUseCase(
    dispatcher = testDispatcher,
  )
}
