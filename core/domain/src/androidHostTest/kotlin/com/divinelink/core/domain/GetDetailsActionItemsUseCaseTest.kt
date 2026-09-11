package com.divinelink.core.domain

import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetDetailsActionItemsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test detail action items for movies`() = runTest {
    val useCase = createUseCase()

    useCase.invoke(parameters = MediaType.MOVIE).let { result ->
      result.isSuccess shouldBe true
      result.getOrNull() shouldContainExactly listOf(
        DetailActionItem.Rate,
        DetailActionItem.Watchlist,
        DetailActionItem.Favorite,
        DetailActionItem.List,
      )
    }
  }

  @Test
  fun `test detail action items for tv`() = runTest {
    val useCase = createUseCase()

    useCase(parameters = MediaType.TV).getOrNull() shouldContainExactly listOf(
      DetailActionItem.Rate,
      DetailActionItem.Watchlist,
      DetailActionItem.Favorite,
      DetailActionItem.List,
      DetailActionItem.AddShow,
    )
  }

  private fun createUseCase() = GetDetailsActionItemsUseCase(
    dispatcher = testDispatcher,
  )
}
