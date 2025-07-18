package com.divinelink.core.data.preferences

import app.cash.turbine.test
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.testing.storage.TestUiSettingsStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProdPreferencesRepositoryTest {

  private val repository = ProdPreferencesRepository(
    storage = TestUiSettingsStorage(),
  )

  @Test
  fun `test default view modes`() = runTest {
    repository.uiPreferences.test {
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
    repository.uiPreferences.test {
      assertThat(awaitItem()).isEqualTo(
        UiPreferences(
          personCreditsViewMode = ViewMode.LIST,
          listsViewMode = ViewMode.LIST,
        ),
      )

      repository.switchViewMode(
        section = ViewableSection.PERSON_CREDITS,
      )

      assertThat(awaitItem()).isEqualTo(
        UiPreferences(
          personCreditsViewMode = ViewMode.GRID,
          listsViewMode = ViewMode.LIST,
        ),
      )

      repository.switchViewMode(
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
