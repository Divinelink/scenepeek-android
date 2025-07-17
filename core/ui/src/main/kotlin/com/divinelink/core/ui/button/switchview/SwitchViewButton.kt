package com.divinelink.core.ui.button.switchview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
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

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    modifier = Modifier
      .clip(shape = MaterialTheme.shapes.large)
      .testTag(TestTags.Components.Button.SWITCH_VIEW)
      .clickable(onClick = onClick)
      .padding(
        vertical = MaterialTheme.dimensions.keyline_8,
        horizontal = MaterialTheme.dimensions.keyline_16,
      ),
  ) {
    Text(
      text = stringResource(R.string.core_ui_view),
      color = MaterialTheme.colorScheme.primary,
    )
    Icon(
      imageVector = icon,
      contentDescription = stringResource(R.string.core_ui_change_layout_button),
      tint = MaterialTheme.colorScheme.primary,
    )
  }
}

@Composable
@Previews
fun SwitchViewButtonPreviews() {
  AppTheme {
    Surface {
      SwitchViewButton(
        section = ViewableSection.LISTS,
        onClick = {},
      )
    }
  }
}
