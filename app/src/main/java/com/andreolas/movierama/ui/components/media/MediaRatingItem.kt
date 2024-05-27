@file:Suppress("MagicNumber")

package com.andreolas.movierama.ui.components.media

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ui.getColorRating
import com.divinelink.core.designsystem.theme.dimensions

@Composable
fun MediaRatingItem(
  modifier: Modifier = Modifier,
  rating: String,
  enlarged: Boolean = false
) {
  val sanitizedRating = if (rating.endsWith(".0")) {
    rating.substring(0, rating.length - 2)
  } else {
    rating
  }

  val color = rating.getColorRating()

  val backgroundColor = if (isSystemInDarkTheme()) {
    MaterialTheme.colorScheme.surface
  } else {
    MaterialTheme.colorScheme.onSurface
  }

  val size = if (enlarged) {
    MaterialTheme.dimensions.keyline_68
  } else {
    MaterialTheme.dimensions.keyline_48
  }

  val ratingCanvasSize = if (enlarged) {
    MaterialTheme.dimensions.keyline_56
  } else {
    MaterialTheme.dimensions.keyline_36
  }

  val textSize = if (enlarged) {
    MaterialTheme.typography.titleMedium
  } else {
    MaterialTheme.typography.labelMedium
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.padding(top = 4.dp, bottom = 4.dp)
  ) {
    Canvas(modifier = Modifier.size(size)) {
      drawArc(
        color = backgroundColor,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
      )
    }

    Canvas(
      modifier = Modifier.size(ratingCanvasSize)
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

    val textColor = if (isSystemInDarkTheme()) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.inverseOnSurface
    }

    Text(
      text = sanitizedRating,
      style = textSize,
      textAlign = TextAlign.Center,
      color = textColor,
    )
  }
}
