package com.divinelink.core.datastore.ui

import app.cash.turbine.test
import com.divinelink.core.model.discover.DiscoverType
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.testing.RoboTest
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DatastoreUiStorageTest : RoboTest() {

  private lateinit var storage: DatastoreUiStorage

  private val testDataStore = TestDatastoreFactory.create()

  @Test
  fun `test initial view modes are all grid`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.allSections.associateWith {
          ViewMode.GRID
        },
      )
    }
  }

  @Test
  fun `test update ViewMode`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.allSections.associateWith {
          ViewMode.GRID
        },
      )

      storage.updateViewMode(
        section = ViewableSection.PersonCredits,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.allSections.associateWith { section ->
          if (section is ViewableSection.PersonCredits) {
            ViewMode.LIST
          } else {
            ViewMode.GRID
          }
        },
      )

      storage.updateViewMode(
        section = ViewableSection.Lists,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.allSections.associateWith { section ->
          if (section is ViewableSection.PersonCredits || section is ViewableSection.Lists) {
            ViewMode.LIST
          } else {
            ViewMode.GRID
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
        viewModes = ViewableSection.allSections.associateWith {
          ViewMode.GRID
        },
      )

      storage.updateViewMode(
        section = SortableSection.Discover.Movies(DiscoverType.Discover),
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.allSections.associateWith {
          when (it) {
            is SortableSection.Discover.Movies,
            is SortableSection.Discover.Shows,
              -> ViewMode.LIST
            else -> ViewMode.GRID
          }
        },
      )

      storage.updateViewMode(
        section = SortableSection.Discover.Shows(DiscoverType.Discover),
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.allSections.associateWith {
          when (it) {
            is SortableSection.Discover -> ViewMode.GRID
            else -> ViewMode.GRID
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
        viewModes = ViewableSection.allSections.associateWith {
          ViewMode.GRID
        },
        sortOption = mapOf(
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
        ),
      )
    }
  }

  @Test
  fun `test update sort direction`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = defaultSortOptions(),
      )

      storage.updateSortDirection(
        SortableSection.Discover.Shows(type = DiscoverType.Upcoming),
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = defaultSortOptions().plus(
          SortableSection.Discover.Shows(DiscoverType.Upcoming) to SortOption(
            sortBy = SortBy.Popularity,
            direction = SortDirection.ASC,
          ),
        ),
      )

      storage.updateSortDirection(
        SortableSection.Discover.Movies(type = DiscoverType.Discover),
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        sortOption = defaultSortOptions()
          .plus(
            SortableSection.Discover.Shows(DiscoverType.Upcoming) to SortOption(
              sortBy = SortBy.Popularity,
              direction = SortDirection.ASC,
            ),
          )
          .plus(
            SortableSection.Discover.Movies(DiscoverType.Discover) to SortOption(
              sortBy = SortBy.Popularity,
              direction = SortDirection.ASC,
            ),
          ),
      )
    }

    @Test
    fun `test update discover sort by options for movies`() = runTest {
      storage = DatastoreUiStorage(testDataStore)

      storage.uiPreferences.test {
        awaitItem() shouldBe UiPreferences.Initial

        storage.updateSortBy(
          section = SortableSection.Discover.Movies(DiscoverType.Discover),
          sortBy = SortBy.ReleaseDate,
        )

        awaitItem() shouldBe UiPreferences.Initial.copy(
          sortOption = defaultSortOptions(),
        )

        storage.updateSortBy(
          section = SortableSection.Discover.Movies(DiscoverType.Discover),
          sortBy = SortBy.VoteCount,
        )

        awaitItem() shouldBe UiPreferences.Initial.copy(
          sortOption = defaultSortOptions()
            .plus(
              SortableSection.Discover.Movies(DiscoverType.Discover) to SortOption(
                SortBy.VoteCount,
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

        storage.updateSortBy(
          section = SortableSection.Discover.Shows(DiscoverType.Discover),
          sortBy = SortBy.FirstAirDate,
        )

        awaitItem() shouldBe UiPreferences.Initial.copy(
          sortOption = defaultSortOptions().plus(
            SortableSection.Discover.Shows(DiscoverType.Discover) to SortOption(
              SortBy.FirstAirDate,
              SortDirection.DESC,
            ),
          ),
        )

        storage.updateSortBy(
          section = SortableSection.Discover.Shows(DiscoverType.Discover),
          sortBy = SortBy.VoteAverage,
        )

        awaitItem() shouldBe UiPreferences.Initial.copy(
          sortOption = defaultSortOptions().plus(
            SortableSection.Discover.Shows(DiscoverType.Discover) to SortOption(
              SortBy.VoteAverage,
              SortDirection.DESC,
            ),
          ),
        )
      }
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
