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
  listId: Int? = null,
  enabled: Boolean = true,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    when (section) {
      ViewableSection.DISCOVER_SHOWS,
      ViewableSection.DISCOVER_MOVIES,
      ViewableSection.USER_DATA,
        -> SortingButton(
        enabled = enabled,
        section = section,
        onSwitchSortDirection = {
          onSwitchPreferences(SwitchPreferencesAction.SwitchSortDirection(it))
        },
        onSwitchSortBy = { section, option ->
          onSwitchPreferences(SwitchPreferencesAction.SwitchSortBy(section, option))
        },
        onClearListSortOption = {
          // Do nothing
        },
      )
      ViewableSection.LIST_DETAILS -> listId?.let { id ->
        SortingButton(
          enabled = enabled,
          section = section,
          listId = listId,
          onSwitchSortDirection = {
            onSwitchPreferences(SwitchPreferencesAction.SwitchListSortDirection(id))
          },
          onSwitchSortBy = { _, option ->
            onSwitchPreferences(SwitchPreferencesAction.SwitchListSortBy(id, option))
          },
          onClearListSortOption = {
            onSwitchPreferences(SwitchPreferencesAction.ClearSortOption(it))
          },
        )
      }
      else -> Unit
    }

    Spacer(Modifier.weight(1f))

    SwitchViewButton(
      section = section,
      onClick = { onSwitchPreferences(SwitchPreferencesAction.SwitchViewMode(it)) },
    )
  }
}
