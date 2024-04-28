package com.andreolas.movierama.settings.appearance.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.settings.app.appearance.usecase.GetThemeUseCase
import com.andreolas.movierama.test.util.fakes.FakePreferenceStorage
import com.andreolas.movierama.ui.theme.Theme
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetThemeUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var fakePreferenceStorage: FakePreferenceStorage

  @Before
  fun setUp() {
    fakePreferenceStorage = FakePreferenceStorage()
  }

  @Test
  fun `correctly get theme use case`() = runTest {
    // Given
    val response = Result.success(Theme.DARK)
    val failResponse = Result.failure<Exception>(Exception("Some exception"))

    fakePreferenceStorage.selectedTheme.value = response.data.storageKey

    // When
    val useCase = GetThemeUseCase(fakePreferenceStorage, testDispatcher)
    val result = useCase(Unit)
    // Then
    assertThat(response).isEqualTo(result)
    assertThat(result).isNotEqualTo(failResponse)
  }
}
