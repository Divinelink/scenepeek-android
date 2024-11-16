package com.divinelink.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@Composable
fun ObfuscateSpoilersButton(
  obfuscated: Boolean,
  onClick: () -> Unit,
) {
  val tooltipText = remember(obfuscated) {
    if (obfuscated) {
      R.string.core_ui_show_spoilers_tooltip
    } else {
      R.string.core_ui_hide_spoilers_tooltip
    }
  }

  IconButtonWithTooltip(
    onClick = onClick,
    modifier = Modifier.testTag(TestTags.Menu.SPOILERS_OBFUSCATION),
    tooltipText = tooltipText,
  ) {
    if (obfuscated) {
      Icon(
        painter = painterResource(id = R.drawable.core_ui_eye_off),
        contentDescription = stringResource(R.string.core_ui_hidden_spoilers_button),
      )
    } else {
      Icon(
        painter = painterResource(R.drawable.core_ui_eye),
        contentDescription = stringResource(R.string.core_ui_visible_spoilers_button),
      )
    }
  }
}

