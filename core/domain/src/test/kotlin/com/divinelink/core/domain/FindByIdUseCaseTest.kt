package com.divinelink.core.domain

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class FindByIdUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestDetailsRepository

  @BeforeTest
  fun setUp() {
    repository = TestDetailsRepository()
  }

  @Test
  fun `test find by id with success`() = runTest {
    repository.mockFindById(Result.success(MediaItemFactory.FightClub()))

    FindByIdUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke("tt123456789").test {
      assertThat(awaitItem()).isEqualTo(Result.success(MediaItemFactory.FightClub()))

      awaitComplete()
    }
  }

  @Test
  fun `test find by id with failure`() = runTest {
    FindByIdUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    ).invoke("tt123456789").test {
      assertThat(awaitItem()).isInstanceOf(Result.failure<Exception>(Exception())::class.java)
      awaitComplete()
    }
  }
}
