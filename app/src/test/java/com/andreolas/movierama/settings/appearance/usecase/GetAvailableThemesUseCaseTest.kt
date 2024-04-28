package com.andreolas.movierama.settings.appearance.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.settings.app.appearance.usecase.GetAvailableThemesUseCase
import com.andreolas.movierama.test.util.fakes.FakePreferenceStorage
import com.andreolas.movierama.ui.theme.Theme
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAvailableThemesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Before
  fun setUp() {
    fakePreferenceStorage = FakePreferenceStorage()
  }

  @Test
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
}
