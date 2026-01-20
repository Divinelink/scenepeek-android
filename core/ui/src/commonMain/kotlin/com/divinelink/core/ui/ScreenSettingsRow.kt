package com.divinelink.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.button.switchview.SortingButton
import com.divinelink.core.ui.button.switchview.SwitchViewButton

@Composable
fun ScreenSettingsRow(
  section: ViewableSection,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    when (section) {
      ViewableSection.DISCOVER_SHOWS -> SortingButton(
        section = section,
        onSwitchSortDirection = {
          onSwitchPreferences(SwitchPreferencesAction.SwitchSortDirection(it))
        },
        onSwitchSortBy = { section, option ->
          onSwitchPreferences(SwitchPreferencesAction.SwitchSortBy(section, option))
        },
      )
      ViewableSection.DISCOVER_MOVIES -> SortingButton(
        section = section,
        onSwitchSortDirection = {
          onSwitchPreferences(SwitchPreferencesAction.SwitchSortDirection(it))
        },
        onSwitchSortBy = { section, option ->
          onSwitchPreferences(SwitchPreferencesAction.SwitchSortBy(section, option))
        },
      )
      else -> Unit
    }

    Spacer(Modifier.weight(1f))

    SwitchViewButton(
      section = section,
      onClick = { onSwitchPreferences(SwitchPreferencesAction.SwitchViewMode(it)) },
    )
  }
}
