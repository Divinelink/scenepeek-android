package com.divinelink.core.domain

import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GetDropdownMenuItemsUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test dropdown menu items without jellyseerr account`() = runTest {
    val preferenceStorage = FakePreferenceStorage(jellyseerrAccount = null)
    val useCase = GetDropdownMenuItemsUseCase(preferenceStorage, testDispatcher)

    useCase.invoke(Unit).first().let { result ->
      assertThat(result.isSuccess).isTrue()
      assertThat(result.getOrNull()).isEqualTo(listOf(DetailsMenuOptions.SHARE))
    }
  }

  @Test
  fun `test dropdown menu items with jellyseerr account`() = runTest {
    val preferenceStorage = FakePreferenceStorage(jellyseerrAccount = "jellyseerr_account")
    val useCase = GetDropdownMenuItemsUseCase(preferenceStorage, testDispatcher)

    useCase.invoke(Unit).first().let { result ->
      assertThat(result.isSuccess).isTrue()
      assertThat(result.getOrNull()).isEqualTo(
        listOf(
          DetailsMenuOptions.SHARE,
          DetailsMenuOptions.REQUEST,
        ),
      )
    }
  }
}
