package com.divinelink.core.ui.components.expanding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.R

object ExpandingComponents {
  @Composable
  fun InlineEdgeFadingEffect(
    modifier: Modifier = Modifier,
    text: String,
  ) {
    Box(modifier = modifier.fillMaxWidth()) {
      val surfaceColor = MaterialTheme.colorScheme.surface

      Canvas(modifier = Modifier.matchParentSize()) {
        drawRect(
          brush = Brush.horizontalGradient(
            colors = listOf(
              Color.Transparent,
              surfaceColor,
            ),
            startX = 0f,
            endX = size.width / 2f,
          ),
          size = this.size,
        )
      }

      Row(
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .padding(start = MaterialTheme.dimensions.keyline_0),
      ) {
        Text(
          modifier = Modifier.align(Alignment.CenterVertically),
          color = MaterialTheme.colorScheme.primary,
          text = text,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyLarge,
        )
        Icon(
          imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
          tint = MaterialTheme.colorScheme.primary,
          contentDescription = text,
        )
      }
    }
  }

  @Composable
  fun ShowLess(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.core_ui_show_less),
    onClick: () -> Unit,
  ) {
    TextButton(
      modifier = modifier,
      onClick = onClick,
    ) {
      Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyLarge,
      )
    }
  }
}
