package com.divinelink.feature.settings.app.appearance

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.domain.theme.GetAvailableColorSystemsUseCase
import com.divinelink.core.domain.theme.GetAvailableThemesUseCase
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.domain.SystemThemeProviderFactory
import com.divinelink.core.testing.domain.theme.material.you.MaterialYouProviderFactory
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_color
import org.jetbrains.compose.resources.getString
import org.junit.Rule
import kotlin.test.Test

class AppearanceSettingsScreenTest : ComposeTest() {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test set custom color system`() = uiTest {
    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      AppearanceSettingsScreen(
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigateUp = {},
      )
    }

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToIndex(3)

    onNodeWithText("Dynamic").assertIsDisplayed()

    onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToIndex(4)

    onNodeWithText("Custom").assertIsDisplayed().performClick()

    onNodeWithText(
      getString(Res.string.feature_settings_color),
    ).assertIsDisplayed()
  }

  private fun setupViewModel(
    themePreferences: ThemePreferences = ThemePreferences.initial,
  ) = AppearanceSettingsViewModel(
    preferencesRepository = TestPreferencesRepository(
      themePreferences = themePreferences,
    ),
    getAvailableThemesUseCase = GetAvailableThemesUseCase(
      systemThemeProvider = SystemThemeProviderFactory.available,
      dispatcher = testDispatcher,
    ),
    getAvailableColorPreferences = GetAvailableColorSystemsUseCase(
      materialYouProvider = MaterialYouProviderFactory.available,
      dispatcher = testDispatcher,
    ),
  )
}
