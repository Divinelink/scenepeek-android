package com.divinelink.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.R

@Composable
fun PlayTrailerButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  TextButton(
    modifier = modifier,
    onClick = onClick,
  ) {
    Icon(Icons.Default.PlayArrow, null)
    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_4))
    Text(text = stringResource(R.string.core_ui_play_trailer))
  }
}
