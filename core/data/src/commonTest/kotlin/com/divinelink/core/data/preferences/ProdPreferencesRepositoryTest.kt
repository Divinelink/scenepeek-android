package com.divinelink.core.data.preferences

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.storage.TestUiSettingsStorage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProdPreferencesRepositoryTest {

  private val repository = ProdPreferencesRepository(
    storage = TestUiSettingsStorage(),
    preferenceStorage = FakePreferenceStorage(),
  )

  @Test
  fun `test default view modes`() = runTest {
    repository.uiPreferences.test {
      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
        ),
      )
    }
  }

  @Test
  fun `test update ViewMode`() = runTest {
    repository.uiPreferences.test {
      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
        ),
      )

      repository.switchViewMode(
        section = ViewableSection.PERSON_CREDITS,
      )

      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.LIST
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
        ),
      )

      repository.switchViewMode(
        section = ViewableSection.LISTS,
      )

      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.LIST
            ViewableSection.PERSON_CREDITS -> ViewMode.LIST
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
        ),
      )
    }
  }

  @Test
  fun `test default theme preferences`() = runTest {
    repository.themePreferences.test {
      awaitItem() shouldBe ThemePreferences.initial
    }
  }

  @Test
  fun `test update current theme`() = runTest {
    repository.updateCurrentTheme(Theme.DARK)

    repository.themePreferences.test {
      awaitItem() shouldBe ThemePreferences.initial.copy(
        theme = Theme.DARK,
      )

      repository.updateCurrentTheme(Theme.LIGHT)

      awaitItem() shouldBe ThemePreferences.initial.copy(
        theme = Theme.LIGHT,
      )
    }
  }

  @Test
  fun `test update color system`() = runTest {
    repository.updateColorSystem(ColorSystem.Custom)

    repository.themePreferences.test {
      awaitItem() shouldBe ThemePreferences.initial.copy(
        colorSystem = ColorSystem.Custom,
      )

      repository.updateColorSystem(ColorSystem.Default)

      awaitItem() shouldBe ThemePreferences.initial.copy(
        colorSystem = ColorSystem.Default,
      )
    }
  }

  @Test
  fun `test update theme color`() = runTest {
    repository.updateThemeColor(Color.DarkGray.value.toLong())

    repository.themePreferences.test {
      awaitItem() shouldBe ThemePreferences.initial.copy(
        themeColor = Color.DarkGray.value.toLong(),
      )

      repository.updateThemeColor(Color.Yellow.value.toLong())

      awaitItem() shouldBe ThemePreferences.initial.copy(
        themeColor = Color.Yellow.value.toLong(),
      )
    }
  }

  @Test
  fun `test set pure black`() = runTest {
    repository.setPureBlack(enabled = true)

    repository.themePreferences.test {
      awaitItem() shouldBe ThemePreferences.initial.copy(
        isPureBlack = true,
      )

      repository.setPureBlack(enabled = false)

      awaitItem() shouldBe ThemePreferences.initial.copy(
        isPureBlack = false,
      )
    }
  }
}
