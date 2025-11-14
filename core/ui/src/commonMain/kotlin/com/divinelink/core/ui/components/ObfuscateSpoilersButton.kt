package com.divinelink.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_eye
import com.divinelink.core.ui.core_ui_eye_off
import com.divinelink.core.ui.core_ui_hidden_spoilers_button
import com.divinelink.core.ui.core_ui_hide_spoilers_tooltip
import com.divinelink.core.ui.core_ui_show_spoilers_tooltip
import com.divinelink.core.ui.core_ui_visible_spoilers_button
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ObfuscateSpoilersButton(
  obfuscated: Boolean,
  onClick: () -> Unit,
) {
  val tooltipText = remember(obfuscated) {
    if (obfuscated) {
      UiString.core_ui_show_spoilers_tooltip
    } else {
      UiString.core_ui_hide_spoilers_tooltip
    }
  }

  IconButtonWithTooltip(
    onClick = onClick,
    modifier = Modifier.testTag(TestTags.Menu.SPOILERS_OBFUSCATION),
    tooltipText = tooltipText,
  ) {
    if (obfuscated) {
      Icon(
        painter = painterResource(UiDrawable.core_ui_eye_off),
        contentDescription = stringResource(UiString.core_ui_hidden_spoilers_button),
      )
    } else {
      Icon(
        painter = painterResource(UiDrawable.core_ui_eye),
        contentDescription = stringResource(UiString.core_ui_visible_spoilers_button),
      )
    }
  }
}
