package com.divinelink.core.datastore.ui

import app.cash.turbine.test
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
  fun `test default view modes`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith { section ->
          when (section) {
            ViewableSection.LISTS -> ViewMode.LIST
            ViewableSection.PERSON_CREDITS -> ViewMode.LIST
            ViewableSection.DISCOVER -> ViewMode.LIST
            ViewableSection.USER_DATA -> ViewMode.LIST
            ViewableSection.MEDIA_DETAILS -> ViewMode.LIST
            ViewableSection.LIST_DETAILS -> ViewMode.LIST
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
            ViewableSection.LISTS -> ViewMode.LIST
            ViewableSection.PERSON_CREDITS -> ViewMode.LIST
            ViewableSection.DISCOVER -> ViewMode.LIST
            ViewableSection.USER_DATA -> ViewMode.LIST
            ViewableSection.MEDIA_DETAILS -> ViewMode.LIST
            ViewableSection.LIST_DETAILS -> ViewMode.LIST
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
            ViewableSection.LISTS -> ViewMode.LIST
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER -> ViewMode.LIST
            ViewableSection.USER_DATA -> ViewMode.LIST
            ViewableSection.MEDIA_DETAILS -> ViewMode.LIST
            ViewableSection.LIST_DETAILS -> ViewMode.LIST
            ViewableSection.SEARCH -> ViewMode.LIST
          }
        },
      )

      storage.updateViewMode(
        section = ViewableSection.LISTS,
      )

      awaitItem() shouldBe UiPreferences.Initial.copy(
        viewModes = ViewableSection.entries.associateWith {
          when (it) {
            ViewableSection.LISTS -> ViewMode.GRID
            ViewableSection.PERSON_CREDITS -> ViewMode.GRID
            ViewableSection.DISCOVER -> ViewMode.LIST
            ViewableSection.USER_DATA -> ViewMode.LIST
            ViewableSection.MEDIA_DETAILS -> ViewMode.LIST
            ViewableSection.LIST_DETAILS -> ViewMode.LIST
            ViewableSection.SEARCH -> ViewMode.GRID
          }
        },
      )
    }
  }
}
