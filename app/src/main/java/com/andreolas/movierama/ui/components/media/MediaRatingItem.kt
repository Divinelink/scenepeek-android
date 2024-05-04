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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun MediaRatingItem(
  modifier: Modifier = Modifier,
  rating: String,
) {
  val sanitizedRating = if (rating.endsWith(".0")) {
    rating.substring(0, rating.length - 2)
  } else {
    rating
  }

  val color = when (rating.toDouble()) {
    in 0.1..3.5 -> Color(0xFFDB2360)
    in 3.5..7.0 -> Color(0xFFD2D531)
    in 7.0..10.0 -> Color(0xFF21D07A)
    else -> Color.White
  }

  val backgroundColor = if (isSystemInDarkTheme()) {
    MaterialTheme.colorScheme.surface
  } else {
    MaterialTheme.colorScheme.onSurface
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.padding(top = 4.dp, bottom = 4.dp)
  ) {
    Canvas(modifier = Modifier.size(MaterialTheme.dimensions.keyline_48)) {
      drawArc(
        color = backgroundColor,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
      )
    }

    Canvas(
      modifier = Modifier.size(36.dp)
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
      style = MaterialTheme.typography.labelMedium,
      textAlign = TextAlign.Center,
      color = textColor,
    )
  }
}
