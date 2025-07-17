package com.divinelink.core.data.preferences

import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

  val uiPreferences: Flow<UiPreferences>

  suspend fun switchViewMode(section: ViewableSection)
}
