package com.divinelink.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.SwitchPreferencesAction.ClearSortOption
import com.divinelink.core.model.ui.SwitchPreferencesAction.SwitchListSortBy
import com.divinelink.core.model.ui.SwitchPreferencesAction.SwitchListSortDirection
import com.divinelink.core.model.ui.SwitchPreferencesAction.SwitchSortBy
import com.divinelink.core.model.ui.SwitchPreferencesAction.SwitchSortDirection
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
      is SortableSection.Discover,
      is SortableSection.UserData,
        -> SortingButton(
          enabled = enabled,
          section = section,
          onSwitchSortDirection = {
            onSwitchPreferences(SwitchSortDirection(it))
          },
          onSwitchSortBy = { section, option ->
            onSwitchPreferences(SwitchSortBy(section, option))
          },
          onClearListSortOption = {
            // Do nothing
          },
        )
      is SortableSection.ListDetails -> listId?.let { id ->
        SortingButton(
          enabled = enabled,
          section = section,
          listId = listId,
          onSwitchSortDirection = {
            onSwitchPreferences(SwitchListSortDirection(id))
          },
          onSwitchSortBy = { _, option ->
            onSwitchPreferences(SwitchListSortBy(id, option))
          },
          onClearListSortOption = {
            onSwitchPreferences(ClearSortOption(it))
          },
        )
      }
      is ViewableSection -> Unit
    }

    Spacer(Modifier.weight(1f))

    SwitchViewButton(
      section = section,
      onClick = { onSwitchPreferences(SwitchPreferencesAction.SwitchViewMode(it)) },
    )
  }
}
