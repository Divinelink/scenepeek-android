package com.andreolas.movierama.settings.appearance.usecase

import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.feature.settings.app.appearance.usecase.SetThemeUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class SetThemeUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Before
  fun setUp() {
    fakePreferenceStorage = FakePreferenceStorage()
  }

  @Test
  fun `correctly set theme use case`() = runTest {
    // Given
    val response = Result.success(Unit)
    val failResponse = Result.failure<Exception>(Exception("Some exception"))
    // When
    val useCase = SetThemeUseCase(fakePreferenceStorage, testDispatcher)
    val result = useCase(Theme.LIGHT)
    // Then
    assertThat(response).isEqualTo(result)
    assertThat(result).isNotEqualTo(failResponse)
  }
}
