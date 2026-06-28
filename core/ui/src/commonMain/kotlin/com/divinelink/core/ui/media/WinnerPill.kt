package com.divinelink.core.ui.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.animations.animatedSpreadShadow
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.resources.Res
import com.divinelink.core.ui.resources.core_ui_winner
import org.jetbrains.compose.resources.stringResource

@Composable
fun WinnerPill(modifier: Modifier = Modifier) {
  CompositionLocalProvider(
    LocalDensity provides Density(
      density = LocalDensity.current.density,
      fontScale = LocalDensity.current.fontScale.coerceIn(1f, 1.35f),
    ),
  ) {
    Box(
      modifier = Modifier
        .animatedSpreadShadow(
          shape = MaterialTheme.shapes.small,
          maxSpread = MaterialTheme.dimensions.keyline_4,
          minSpread = MaterialTheme.dimensions.keyline_0,
          durationMillis = 1800,
        )
        .background(
          color = MaterialTheme.colors.mediumSeaGreen,
          shape = MaterialTheme.shapes.small,
        )
        .padding(
          vertical = MaterialTheme.dimensions.keyline_4,
          horizontal = MaterialTheme.dimensions.keyline_12,
        ),
    ) {
      Text(
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
        textAlign = TextAlign.Center,
        text = stringResource(Res.string.core_ui_winner),
        color = Color.White,
      )
    }
  }
}

@Previews
@Composable
fun WinnerPillPreview() {
  PreviewLocalProvider {
    WinnerPill()
  }
}
