package com.divinelink.core.ui.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import com.divinelink.core.commons.extensions.isWholeNumber
import com.divinelink.core.commons.extensions.round
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews

@Composable
fun DiscreetRatingItem(
  modifier: Modifier = Modifier,
  rating: Double?,
) {
  val sanitizedRating = if (rating == null) {
    null
  } else {
    if (rating.isWholeNumber()) {
      rating.toInt()
    } else {
      rating.round(1)
    }
  }

  if (sanitizedRating == 0 || sanitizedRating?.toString() == null) return

  val backgroundColor = if (LocalDarkThemeProvider.current) {
    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
  } else {
    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
  }

  val textColor = if (LocalDarkThemeProvider.current) {
    MaterialTheme.colorScheme.onSurface
  } else {
    MaterialTheme.colorScheme.surface
  }

  CompositionLocalProvider(
    LocalDensity provides Density(
      density = LocalDensity.current.density,
      fontScale = LocalDensity.current.fontScale.coerceIn(1f, 1.35f),
    ),
  ) {
    Surface(
      modifier = modifier.widthIn(min = MaterialTheme.dimensions.keyline_26),
      color = backgroundColor,
      shape = MaterialTheme.shapes.small,

    ) {
      Text(
        modifier = Modifier.padding(MaterialTheme.dimensions.keyline_4),
        text = sanitizedRating.toString(),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
        textAlign = TextAlign.Center,
        color = textColor,
      )
    }
  }
}

@Previews
@Composable
fun DiscreetRatingItemPreview() {
  AppTheme {
    Surface {
      Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        DiscreetRatingItem(rating = null)
        DiscreetRatingItem(rating = 1.1)
        DiscreetRatingItem(rating = 5.4)
        DiscreetRatingItem(rating = 9.2)
      }
    }
  }
}
