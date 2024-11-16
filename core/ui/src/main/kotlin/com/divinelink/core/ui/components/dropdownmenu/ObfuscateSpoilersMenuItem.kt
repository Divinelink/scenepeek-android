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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@Composable
fun ObfuscateSpoilersMenuItem(
  obfuscated: Boolean,
  onClick: () -> Unit,
) {
  val textRes = remember(obfuscated) {
    if (obfuscated) {
      R.string.core_ui_show_total_episodes_item
    } else {
      R.string.core_ui_hide_total_episodes_item
    }
  }

  DropdownMenuItem(
    modifier = Modifier.testTag(
      TestTags.Menu.MENU_ITEM.format(stringResource(id = textRes)),
    ),
    text = { Text(text = stringResource(id = textRes)) },
    leadingIcon = {
      if (obfuscated) {
        Icon(
          painter = painterResource(R.drawable.core_ui_eye),
          contentDescription = stringResource(R.string.core_ui_visible_spoilers_button),
        )
      } else {
        Icon(
          painter = painterResource(id = R.drawable.core_ui_eye_off),
          contentDescription = stringResource(R.string.core_ui_hidden_spoilers_button),
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
