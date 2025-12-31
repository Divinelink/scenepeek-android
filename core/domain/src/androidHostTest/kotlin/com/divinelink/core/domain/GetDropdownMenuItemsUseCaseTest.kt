package com.divinelink.core.domain

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class GetDropdownMenuItemsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test dropdown menu items for movie does not include spoilers action`() = runTest {
    val useCase = GetDropdownMenuItemsUseCase(testDispatcher)

    useCase.invoke(MediaType.MOVIE).first().let { result ->
      assertThat(result.isSuccess).isTrue()
      assertThat(result.getOrNull()).isEqualTo(listOf(DetailsMenuOptions.SHARE))
    }
  }

  @Test
  fun `test dropdown menu items for tv media shows spoiler action`() = runTest {
    val useCase = GetDropdownMenuItemsUseCase(testDispatcher)

    useCase.invoke(MediaType.TV).first().let { result ->
      assertThat(result.isSuccess).isTrue()
      assertThat(result.getOrNull()).isEqualTo(
        listOf(
          DetailsMenuOptions.OBFUSCATE_SPOILERS,
          DetailsMenuOptions.SHARE,
        ),
      )
    }
  }
}
