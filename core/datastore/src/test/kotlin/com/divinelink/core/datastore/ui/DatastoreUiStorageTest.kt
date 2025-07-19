package com.divinelink.core.datastore.ui

import app.cash.turbine.test
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import com.google.common.truth.Truth.assertThat
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
      assertThat(awaitItem()).isEqualTo(
        UiPreferences(
          personCreditsViewMode = ViewMode.LIST,
          listsViewMode = ViewMode.LIST,
        ),
      )
    }
  }

  @Test
  fun `test update ViewMode`() = runTest {
    storage = DatastoreUiStorage(testDataStore)

    storage.uiPreferences.test {
      assertThat(awaitItem()).isEqualTo(
        UiPreferences(
          personCreditsViewMode = ViewMode.LIST,
          listsViewMode = ViewMode.LIST,
        ),
      )

      storage.updateViewMode(
        section = ViewableSection.PERSON_CREDITS,
      )

      assertThat(awaitItem()).isEqualTo(
        UiPreferences(
          personCreditsViewMode = ViewMode.GRID,
          listsViewMode = ViewMode.LIST,
        ),
      )

      storage.updateViewMode(
        section = ViewableSection.LISTS,
      )

      assertThat(awaitItem()).isEqualTo(
        UiPreferences(
          personCreditsViewMode = ViewMode.GRID,
          listsViewMode = ViewMode.GRID,
        ),
      )
    }
  }
}
