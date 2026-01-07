package com.divinelink.scenepeek.settings.appearance.ui

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.designsystem.theme.seedLong
import com.divinelink.core.domain.theme.GetAvailableColorSystemsUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.domain.theme.ProdSystemThemeProvider
import com.divinelink.core.domain.theme.SystemThemeProvider
import com.divinelink.core.domain.theme.material.you.MaterialYouProvider
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.domain.theme.material.you.MaterialYouProviderFactory
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsViewModel
import com.divinelink.feature.settings.app.appearance.UpdateSettingsState
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class AppearanceSettingsViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var viewModel: AppearanceSettingsViewModel

  private fun buildViewModel(
    themePreferences: ThemePreferences = ThemePreferences.initial,
    materialYouProvider: MaterialYouProvider = MaterialYouProviderFactory.available,
    systemThemeProvider: SystemThemeProvider = ProdSystemThemeProvider(),
  ) = apply {
    viewModel = AppearanceSettingsViewModel(
      getAvailableThemesUseCase = GetAvailableThemesUseCase(
        systemThemeProvider = systemThemeProvider,
        dispatcher = testDispatcher,
      ),
      getAvailableColorPreferences = GetAvailableColorSystemsUseCase(
        materialYouProvider = materialYouProvider,
        dispatcher = testDispatcher,
      ),
      preferencesRepository = TestPreferencesRepository(
        themePreferences = themePreferences,
      ),
    )
  }

  @Test
  fun `given theme is system, when I set theme to dark, then I expect dark theme`() = runTest {
    // Given
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(theme = Theme.SYSTEM),
    )
    // When
    viewModel.setTheme(Theme.DARK)
    // Then
    val state = viewModel.uiState.first()
    state.assertState(theme = Theme.DARK)
  }

  @Test
  fun `given theme is system, then I expect system theme`() = runTest {
    // Given
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(theme = Theme.SYSTEM),
    )
    // Then
    val state = viewModel.uiState.first()
    state.assertState(theme = Theme.SYSTEM)
  }

  @Test
  fun `given theme is dark, then I expect dark theme`() = runTest {
    // Given
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(theme = Theme.DARK),
    )
    // Then
    val state = viewModel.uiState.first()
    state.assertState(theme = Theme.DARK)
  }

  @Test
  fun `given theme is light, then I expect light theme`() = runTest {
    // Given
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(theme = Theme.LIGHT),
    )
    // Then
    val state = viewModel.uiState.first()
    state.assertState(theme = Theme.LIGHT)
  }

  @Test
  fun `given dynamic color is enabled, when I update it I expect updated color system`() = runTest {
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(
        colorSystem = ColorSystem.Dynamic,
      ),
    )

    viewModel.uiState.test {
      awaitItem().assertState(colorSystem = ColorSystem.Dynamic)
      viewModel.updateColorSystem(ColorSystem.Custom)
      awaitItem().assertState(colorSystem = ColorSystem.Custom)
      viewModel.updateColorSystem(ColorSystem.Default)
      awaitItem().assertState(colorSystem = ColorSystem.Default)
    }
  }

  @Test
  fun `given black backgrounds is enabled, when I disable it, then I expect false`() = runTest {
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(
        isPureBlack = true,
      ),
    )
    viewModel.uiState.test {
      awaitItem().assertState(isPureBlack = true)
      viewModel.setBlackBackgrounds(false)
      awaitItem().assertState(isPureBlack = false)
    }
  }

  @Test
  fun `test dynamic color is unavailable`() = runTest {
    buildViewModel(
      materialYouProvider = MaterialYouProviderFactory.unavailable,
    )

    val state = viewModel.uiState.first()
    state.assertState(
      availableColorSystems = listOf(
        ColorSystem.Default,
        ColorSystem.Custom,
      ),
    )
  }

  @Test
  fun `test dynamic color is available`() = runTest {
    buildViewModel(
      materialYouProvider = MaterialYouProviderFactory.available,
    )

    val state = viewModel.uiState.first()
    state.assertState(
      availableColorSystems = listOf(
        ColorSystem.Default,
        ColorSystem.Dynamic,
        ColorSystem.Custom,
      ),
    )
  }

  @Test
  fun `test check and update theme color`() = runTest {
    buildViewModel(
      themePreferences = ThemePreferences.initial.copy(
        themeColor = Color.Green.value.toLong(),
      ),
    )
    viewModel.uiState.test {
      awaitItem().assertState(themeColor = Color.Green.value.toLong())
      viewModel.setThemeColor(Color.Magenta.value.toLong())
      awaitItem().assertState(themeColor = Color.Magenta.value.toLong())
    }
  }

  private fun UpdateSettingsState.assertState(
    theme: Theme = Theme.SYSTEM,
    availableThemes: List<Theme> = listOf(Theme.LIGHT, Theme.DARK),
    colorSystem: ColorSystem = ColorSystem.Dynamic,
    themeColor: Long = seedLong,
    isPureBlack: Boolean = false,
    availableColorSystems: List<ColorSystem> = ColorSystem.entries,
  ) {
    this.themePreferences.theme shouldBe theme
    this.themePreferences.colorSystem shouldBe colorSystem
    this.themePreferences.themeColor shouldBe themeColor
    this.themePreferences.isPureBlack shouldBe isPureBlack
    this.availableThemes shouldBe availableThemes
    this.availableColorSystems shouldBe availableColorSystems
  }
}
