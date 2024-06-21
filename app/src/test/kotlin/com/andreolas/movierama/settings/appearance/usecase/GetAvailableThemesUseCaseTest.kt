package com.andreolas.movierama.settings.appearance.usecase

import android.os.Build
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.UnitTest
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.feature.settings.app.appearance.usecase.GetAvailableThemesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class GetAvailableThemesUseCaseTest : UnitTest() {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Before
  fun setUp() {
    fakePreferenceStorage = FakePreferenceStorage()
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.P])
  fun `given I have SDK_INT less than 29, then I see only two theme options`() = runTest {
    // Given
    val response = Result.success(listOf(Theme.LIGHT, Theme.DARK))
    val failResponse = Result.failure<Exception>(Exception("Some exception"))

    // When
    val useCase = GetAvailableThemesUseCase(testDispatcher)
    val result = useCase(Unit)

    // Then
    assertThat(response).isEqualTo(result)
    assertThat(result).isNotEqualTo(failResponse)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.R])
  fun `given I have SDK greater than 29, then I system theme is available`() = runTest {
    // Given
    val response = Result.success(listOf(Theme.SYSTEM, Theme.LIGHT, Theme.DARK))
    val failResponse = Result.failure<Exception>(Exception("Some exception"))

    // When
    val useCase = GetAvailableThemesUseCase(testDispatcher)
    val result = useCase(Unit)

    // Then
    assertThat(response).isEqualTo(result)
    assertThat(result).isNotEqualTo(failResponse)
  }
}
