package com.divinelink.core.fixtures.data.preferences

import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestPreferencesRepository(uiPreferences: UiPreferences = UiPreferences.Initial) :
  PreferencesRepository {

  private val uiPreferencesFlow = MutableStateFlow(uiPreferences)

  override val uiPreferences: Flow<UiPreferences> = uiPreferencesFlow

  override suspend fun switchViewMode(section: ViewableSection) {
    val currentViewMode = when (section) {
      ViewableSection.PERSON_CREDITS -> uiPreferencesFlow.value.personCreditsViewMode
      ViewableSection.LISTS -> uiPreferencesFlow.value.listsViewMode
    }

    uiPreferencesFlow.value = when (section) {
      ViewableSection.PERSON_CREDITS -> uiPreferencesFlow.value.copy(
        personCreditsViewMode = currentViewMode.other(),
      )
      ViewableSection.LISTS -> uiPreferencesFlow.value.copy(
        listsViewMode = currentViewMode.other(),
      )
    }
  }
}
