package com.divinelink.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonWithTooltip(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  @StringRes tooltipText: Int,
  icon: @Composable () -> Unit,
) {
  TooltipBox(
    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    tooltip = {
      RichTooltip {
        Text(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_8),
          text = stringResource(tooltipText),
        )
      }
    },
    state = rememberTooltipState(),
  ) {
    IconButton(
      modifier = modifier,
      onClick = onClick,
    ) {
      icon()
    }
  }
}
