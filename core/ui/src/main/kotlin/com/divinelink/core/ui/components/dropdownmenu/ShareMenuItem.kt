package com.divinelink.core.ui.components.dropdownmenu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@Composable
fun ShareMenuItem(onClick: () -> Unit) {
  DropdownMenuItem(
    modifier = Modifier.testTag(
      TestTags.Menu.MENU_ITEM.format(stringResource(id = R.string.core_ui_share)),
    ),
    text = { Text(text = stringResource(id = R.string.core_ui_share)) },
    onClick = onClick,
  )
}
