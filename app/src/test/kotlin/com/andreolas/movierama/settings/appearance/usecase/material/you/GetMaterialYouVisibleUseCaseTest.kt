package com.andreolas.movierama.settings.appearance.usecase.material.you

import android.os.Build
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.UnitTest
import com.divinelink.feature.settings.app.appearance.usecase.material.you.GetMaterialYouVisibleUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class GetMaterialYouVisibleUseCaseTest : UnitTest() {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q])
  fun `given I have SDK_INT less than 31, then I expect MaterialYou not visible`() = runTest {
    // Given
    val response = Result.success(false)

    // When
    val useCase = GetMaterialYouVisibleUseCase(testDispatcher)
    val result = useCase(Unit)

    // Then
    assertThat(response).isEqualTo(result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.S])
  fun `given I have SDK_INT greater than 30, then I expect MaterialYou visible`() = runTest {
    // Given
    val response = Result.success(true)

    // When
    val useCase = GetMaterialYouVisibleUseCase(testDispatcher)
    val result = useCase(Unit)

    // Then
    assertThat(response).isEqualTo(result)
  }
}
