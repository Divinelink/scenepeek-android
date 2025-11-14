package com.divinelink.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_play_trailer
import org.jetbrains.compose.resources.stringResource

@Composable
fun WatchTrailerButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  TextButton(
    modifier = modifier.testTag(TestTags.Details.WATCH_TRAILER),
    onClick = onClick,
  ) {
    Icon(Icons.Default.SmartDisplay, null)
    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_4))
    Text(text = stringResource(UiString.core_ui_play_trailer))
  }
}
