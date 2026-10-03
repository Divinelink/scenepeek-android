package com.divinelink.core.data.preferences

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.SortableSection
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
        viewModes = ViewableSection.allSections.associateWith {
          ViewMode.GRID
        },
        sortOption = defaultSortOptions(),
        listSortOptions = mapOf(),
        expanded = ExpandableSection.entries.associateWith {
          false
        },
      )
    }
  }

  @Test
  fun `test update ViewMode`() = runTest {
    repository.uiPreferences.test {
      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.allSections.associateWith {
          when (it) {
            is SortableSection.Discover.Movies -> ViewMode.GRID
            is SortableSection.Discover.Shows -> ViewMode.GRID
            SortableSection.ListDetails -> ViewMode.GRID
            SortableSection.UserData -> ViewMode.GRID
            ViewableSection.Lists -> ViewMode.GRID
            ViewableSection.MediaDetails -> ViewMode.GRID
            ViewableSection.PersonCredits -> ViewMode.GRID
            ViewableSection.Search -> ViewMode.GRID
          }
        },
        sortOption = defaultSortOptions(),
        listSortOptions = mapOf(),
        expanded = ExpandableSection.entries.associateWith {
          false
        },
      )

      repository.switchViewMode(
        section = ViewableSection.PersonCredits,
      )

      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.allSections.associateWith {
          when (it) {
            is SortableSection.Discover.Movies -> ViewMode.GRID
            is SortableSection.Discover.Shows -> ViewMode.GRID
            SortableSection.ListDetails -> ViewMode.GRID
            SortableSection.UserData -> ViewMode.GRID
            ViewableSection.Lists -> ViewMode.GRID
            ViewableSection.MediaDetails -> ViewMode.GRID
            ViewableSection.PersonCredits -> ViewMode.LIST
            ViewableSection.Search -> ViewMode.GRID
          }
        },
        sortOption = defaultSortOptions(),
        listSortOptions = mapOf(),
        expanded = ExpandableSection.entries.associateWith {
          false
        },
      )

      repository.switchViewMode(
        section = ViewableSection.Lists,
      )

      awaitItem() shouldBe UiPreferences(
        viewModes = ViewableSection.allSections.associateWith {
          when (it) {
            is SortableSection.Discover.Movies -> ViewMode.GRID
            is SortableSection.Discover.Shows -> ViewMode.GRID
            SortableSection.ListDetails -> ViewMode.GRID
            SortableSection.UserData -> ViewMode.GRID
            ViewableSection.Lists -> ViewMode.LIST
            ViewableSection.MediaDetails -> ViewMode.GRID
            ViewableSection.PersonCredits -> ViewMode.LIST
            ViewableSection.Search -> ViewMode.GRID
          }
        },
        sortOption = defaultSortOptions(),
        listSortOptions = mapOf(),
        expanded = ExpandableSection.entries.associateWith {
          false
        },
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

  private fun defaultSortOptions() = mapOf(
    SortableSection.UserData to SortOption(SortBy.CreatedAt, SortDirection.DESC),
    SortableSection.Discover.Movies(DiscoverType.Discover) to SortOption(
      SortBy.Popularity,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Shows(DiscoverType.Discover) to SortOption(
      SortBy.Popularity,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Movies(DiscoverType.Upcoming) to SortOption(
      SortBy.Popularity,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Shows(DiscoverType.Upcoming) to SortOption(
      SortBy.Popularity,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Movies(DiscoverType.Popular) to SortOption(
      SortBy.Popularity,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Shows(DiscoverType.Popular) to SortOption(
      SortBy.Popularity,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Movies(DiscoverType.TopRated) to SortOption(
      SortBy.VoteAverage,
      SortDirection.DESC,
    ),
    SortableSection.Discover.Shows(DiscoverType.TopRated) to SortOption(
      SortBy.VoteAverage,
      SortDirection.DESC,
    ),
  )
}
