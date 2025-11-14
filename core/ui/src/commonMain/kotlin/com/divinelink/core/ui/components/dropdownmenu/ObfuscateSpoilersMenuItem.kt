package com.divinelink.core.ui.components.dropdownmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_eye
import com.divinelink.core.ui.core_ui_eye_off
import com.divinelink.core.ui.core_ui_hidden_spoilers_button
import com.divinelink.core.ui.core_ui_hide_total_episodes_item
import com.divinelink.core.ui.core_ui_show_total_episodes_item
import com.divinelink.core.ui.core_ui_visible_spoilers_button
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ObfuscateSpoilersMenuItem(
  obfuscated: Boolean,
  onClick: () -> Unit,
) {
  val textRes = remember(obfuscated) {
    if (obfuscated) {
      UiString.core_ui_show_total_episodes_item
    } else {
      UiString.core_ui_hide_total_episodes_item
    }
  }

  DropdownMenuItem(
    modifier = Modifier
      .testTag(TestTags.Menu.item(stringResource(textRes))),
    text = { Text(text = stringResource(textRes)) },
    leadingIcon = {
      if (obfuscated) {
        Icon(
          painter = painterResource(UiDrawable.core_ui_eye),
          tint = MaterialTheme.colorScheme.onSurface,
          contentDescription = stringResource(UiString.core_ui_visible_spoilers_button),
        )
      } else {
        Icon(
          painter = painterResource(UiDrawable.core_ui_eye_off),
          tint = MaterialTheme.colorScheme.onSurface,
          contentDescription = stringResource(UiString.core_ui_hidden_spoilers_button),
        )
      }
    },
    onClick = onClick,
  )
}

@Previews
@Composable
fun ObfuscateTotalEpisodesMenuItemPreview() {
  AppTheme {
    Surface {
      Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        ObfuscateSpoilersMenuItem(
          obfuscated = false,
          onClick = {},
        )

        ObfuscateSpoilersMenuItem(
          obfuscated = true,
          onClick = {},
        )
      }
    }
  }
}
