package com.divinelink.core.ui.button.switchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.local.LocalUiPreferences
import com.divinelink.core.ui.local.rememberViewModePreferences

@Composable
fun SwitchViewButton(
  section: ViewableSection,
  onClick: () -> Unit,
) {
  val isGrid = rememberViewModePreferences(section)
  val icon = remember(isGrid) {
    if (isGrid == ViewMode.GRID) {
      Icons.AutoMirrored.Outlined.List
    } else {
      Icons.Outlined.GridView
    }
  }

  IconButton(
    modifier = Modifier
      .clip(shape = MaterialTheme.shapes.large)
      .testTag(TestTags.Components.Button.SWITCH_VIEW),
    onClick = onClick,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = stringResource(R.string.core_ui_change_layout_button),
      tint = MaterialTheme.colorScheme.primary,
    )
  }
}

@Composable
@Previews
fun SwitchViewButtonListPreviews() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      ) {
        CompositionLocalProvider(
          LocalUiPreferences provides UiPreferences.Initial.copy(
            listsViewMode = ViewMode.GRID,
          ),
        ) {
          SwitchViewButton(
            section = ViewableSection.LISTS,
            onClick = {},
          )
        }

        CompositionLocalProvider(
          LocalUiPreferences provides UiPreferences.Initial.copy(
            listsViewMode = ViewMode.LIST,
          ),
        ) {
          SwitchViewButton(
            section = ViewableSection.LISTS,
            onClick = {},
          )
        }
      }
    }
  }
}
