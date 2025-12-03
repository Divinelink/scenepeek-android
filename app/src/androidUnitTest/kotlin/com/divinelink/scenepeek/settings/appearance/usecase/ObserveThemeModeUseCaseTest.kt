package com.divinelink.scenepeek.settings.appearance.usecase

import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.domain.theme.ObserveThemeModeUseCase
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class ObserveThemeModeUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Test
  fun `correct theme is observed`() = runTest {
    // Given
    val response = Result.success(Theme.LIGHT)

    fakePreferenceStorage = FakePreferenceStorage(
      selectedTheme = Theme.LIGHT.storageKey,
    )

    // When
    val useCase = ObserveThemeModeUseCase(fakePreferenceStorage, testDispatcher)
    val result = useCase(Unit)

    // Then
    Truth.assertThat(response).isEqualTo(result.first())
  }

  @Test
  fun `correct theme is observed - dark`() = runTest {
    // Given
    val response = Result.success(Theme.DARK)

    fakePreferenceStorage = FakePreferenceStorage(
      selectedTheme = Theme.DARK.storageKey,
    )

    // When
    val useCase = ObserveThemeModeUseCase(fakePreferenceStorage, testDispatcher)
    val result = useCase(Unit)

    // Then
    Truth.assertThat(response).isEqualTo(result.first())
  }

  @Test
  fun `correct theme is observed - system`() = runTest {
    // Given
    val response = Result.success(Theme.SYSTEM)

    fakePreferenceStorage = FakePreferenceStorage(
      selectedTheme = Theme.SYSTEM.storageKey,
    )

    // When
    val useCase = ObserveThemeModeUseCase(fakePreferenceStorage, testDispatcher)
    val result = useCase(Unit)

    // Then
    Truth.assertThat(response).isEqualTo(result.first())
  }
}
