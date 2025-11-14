package com.divinelink.core.ui.components.dropdownmenu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_share
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShareMenuItem(onClick: () -> Unit) {
  DropdownMenuItem(
    modifier = Modifier.testTag(
      TestTags.Menu.item(stringResource(UiString.core_ui_share)),
    ),
    text = { Text(text = stringResource(UiString.core_ui_share)) },
    onClick = onClick,
  )
}
