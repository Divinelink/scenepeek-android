package com.divinelink.core.datastore.ui

import app.cash.turbine.test
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class DatastoreUiStorageTest {

  private lateinit var storage: DatastoreUiStorage

  private val testDataStore = TestDatastoreFactory.create()

  @Test
  fun `test initial view modes are all grid`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith { section ->
          when (section) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )
    }
  }

  @Test
  fun `test update ViewMode`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith { section ->
          when (section) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )

      storage.updateViewMode(
        section = ViewableSection.PERSON_CREDITS,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.LIST
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )

      storage.updateViewMode(
        section = ViewableSection.LISTS,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.LIST
            ViewableSection.PERSON_CREDITS -> ViewMode.LIST
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )
    }
  }

  @Test
  fun `test update view mode for discover movies also switches for shows`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith { section ->
          when (section) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )

      storage.updateViewMode(
        section = ViewableSection.DISCOVER_MOVIES,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.LIST
            ViewableSection.DISCOVER_SHOWS -> ViewMode.LIST
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )

      storage.updateViewMode(
        section = ViewableSection.DISCOVER_SHOWS,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
            ViewableSection.USER_DATA -> ViewMode.GRID
            ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
            ViewableSection.LIST_DETAILS -> ViewMode.GRID
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )
    }
  }

  @Test
  fun `test initial discover sort options are popular desc`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
            ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
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
  fun `test update sort direction`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
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

      storage.updateSortDirection(ViewableSection.DISCOVER_MOVIES)

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.ASC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
        ),
      )

      storage.updateSortDirection(ViewableSection.DISCOVER_SHOWS)

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.ASC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.ASC,
          ),
        ),
      )
    }
  }

  @Test
  fun `test update discover sort by options for movies`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial

      storage.updateSortBy(ViewableSection.DISCOVER_MOVIES, sortBy = SortBy.RELEASE_DATE)

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.RELEASE_DATE,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
        ),
      )

      storage.updateSortBy(ViewableSection.DISCOVER_MOVIES, sortBy = SortBy.VOTE_AVERAGE)

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.VOTE_AVERAGE,
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
  fun `test update discover sort by options for shows`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial

      storage.updateSortBy(ViewableSection.DISCOVER_SHOWS, sortBy = SortBy.FIRST_AIR_DATE)

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.FIRST_AIR_DATE,
            SortDirection.DESC,
          ),
        ),
      )

      storage.updateSortBy(ViewableSection.DISCOVER_SHOWS, sortBy = SortBy.VOTE_AVERAGE)

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = mapOf(
          ViewableSection.DISCOVER_MOVIES to SortOption(
            SortBy.POPULARITY,
            SortDirection.DESC,
          ),
          ViewableSection.DISCOVER_SHOWS to SortOption(
            SortBy.VOTE_AVERAGE,
            SortDirection.DESC,
          ),
        ),
      )
    }
  }
}
