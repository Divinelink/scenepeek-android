package com.divinelink.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_navigate_up_button_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun NavigateUpButton(onClick: () -> Unit) {
  IconButton(
    modifier = Modifier.testTag(TestTags.Settings.NAVIGATION_ICON),
    onClick = onClick,
  ) {
    Icon(
      imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
      contentDescription = stringResource(UiString.core_ui_navigate_up_button_content_description),
    )
  }
}
