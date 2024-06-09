@file:Suppress("MagicNumber")

package com.divinelink.ui

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.ui.extension.getColorRating

@Suppress("MagicNumber")
enum class RatingSize(
  val size: Dp,
  val coloredArcSize: Dp,
) {
  SMALL(
    size = 40.dp,
    coloredArcSize = 28.dp
  ),
  MEDIUM(
    size = 48.dp,
    coloredArcSize = 36.dp
  ),
  LARGE(
    size = 68.dp,
    coloredArcSize = 56.dp
  )
}

@Composable
fun MediaRatingItem(
  modifier: Modifier = Modifier,
  rating: String,
  size: RatingSize = RatingSize.MEDIUM
) {
  val sanitizedRating = if (rating.endsWith(".0")) {
    rating.substring(0, rating.length - 2)
  } else {
    rating
  }

  val color = rating.getColorRating()

  val backgroundColor = if (LocalDarkThemeProvider.current) {
    MaterialTheme.colorScheme.surface
  } else {
    MaterialTheme.colorScheme.onSurface
  }

  val textSize = when (size) {
    RatingSize.SMALL -> MaterialTheme.typography.labelMedium
    RatingSize.MEDIUM -> MaterialTheme.typography.labelMedium
    RatingSize.LARGE -> MaterialTheme.typography.titleMedium
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.padding(top = 4.dp, bottom = 4.dp)
  ) {
    Canvas(modifier = Modifier.size(size.size)) {
      drawArc(
        color = backgroundColor,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
      )
    }

    Canvas(
      modifier = Modifier.size(size.coloredArcSize)
    ) {
      drawArc(
        color = color.copy(alpha = 0.3f),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
        style = Stroke(
          width = 4.dp.toPx(),
          miter = 4f,
        )
      )
      drawArc(
        color = color,
        startAngle = 270f,
        sweepAngle = (100f / 10f * rating.toDouble() * 3.6f).toFloat(),
        useCenter = false,
        style = Stroke(
          width = 4.dp.toPx(),
          miter = 2f,
          cap = StrokeCap.Round,
        )
      )
    }

    val textColor = if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.surface
    }

    Text(
      text = sanitizedRating,
      style = textSize,
      textAlign = TextAlign.Center,
      color = textColor,
    )
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun MediaRatingItemPreview() {
  AppTheme {
    Surface {
      Row {
        MediaRatingItem(rating = "5.4", size = RatingSize.SMALL)
        MediaRatingItem(rating = "5", size = RatingSize.MEDIUM)
        MediaRatingItem(rating = "5", size = RatingSize.LARGE)
      }
    }
  }
}
