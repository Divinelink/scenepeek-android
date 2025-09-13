package com.divinelink.core.domain

import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetDetailsActionItemsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val authRepository: TestAuthRepository = TestAuthRepository()

  @Test
  fun `test detail action items without jellyseerr account`() = runTest {
    authRepository.mockJellyseerrEnabled(false)

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

  @Test
  fun `test detail action items with jellyseerr account`() = runTest {
    authRepository.mockJellyseerrEnabled(true)

    val useCase = createUseCase()

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

  private fun createUseCase() = GetDetailsActionItemsUseCase(
    authRepository = authRepository.mock,
    dispatcher = testDispatcher,
  )
}
