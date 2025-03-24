@file:Suppress("MagicNumber")

package com.divinelink.core.ui.rating

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.divinelink.core.commons.extensions.isWholeNumber
import com.divinelink.core.commons.extensions.round
import com.divinelink.core.commons.extensions.toShortString
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.getColorRating

@Suppress("MagicNumber")
enum class RatingSize(
  val size: Dp,
  val coloredArcSize: Dp,
) {
  SMALL(
    size = 40.dp,
    coloredArcSize = 28.dp,
  ),
  MEDIUM(
    size = 48.dp,
    coloredArcSize = 36.dp,
  ),
  LARGE(
    size = 68.dp,
    coloredArcSize = 56.dp,
  ),
}

@Composable
fun TMDBRatingItem(
  modifier: Modifier = Modifier,
  rating: Double?,
  voteCount: Int?,
  size: RatingSize = RatingSize.MEDIUM,
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
    modifier = modifier
      .testTag(TestTags.Rating.TMDB_RATING)
      .padding(vertical = MaterialTheme.dimensions.keyline_4),
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
      modifier = Modifier.size(size.coloredArcSize),
    ) {
      drawArc(
        color = color.copy(alpha = 0.3f),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
        style = Stroke(
          width = 4.dp.toPx(),
          miter = 4f,
        ),
      )
      drawArc(
        color = color,
        startAngle = 270f,
        sweepAngle = (100f / 10f * (rating?.round(1) ?: 0.0) * 3.6f).toFloat(),
        useCenter = false,
        style = Stroke(
          width = 4.dp.toPx(),
          miter = 2f,
          cap = StrokeCap.Round,
        ),
      )
    }

    val textColor = if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.surface
    }

    val votesColor = if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
    } else {
      MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      CompositionLocalProvider(
        LocalDensity provides Density(
          density = LocalDensity.current.density,
          fontScale = LocalDensity.current.fontScale.coerceIn(1f, 1.35f),
        ),
      ) {
        Text(
          text = sanitizedRating?.toString() ?: "-",
          style = textSize,
          textAlign = TextAlign.Center,
          color = textColor,
        )

        if (voteCount != null && sanitizedRating != null && size == RatingSize.LARGE) {
          Text(
            modifier = Modifier.testTag(TestTags.Rating.VOTE_COUNT),
            text = voteCount.toShortString(),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = votesColor,
          )
        }
      }
    }
  }
}

@Previews
@Composable
fun TMDBRatingItemPreview() {
  AppTheme {
    Surface {
      Column {
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
          TMDBRatingItem(rating = null, size = RatingSize.SMALL, voteCount = null)
          TMDBRatingItem(rating = null, size = RatingSize.MEDIUM, voteCount = null)
          TMDBRatingItem(rating = null, size = RatingSize.LARGE, voteCount = null)
          TMDBRatingItem(rating = null, size = RatingSize.LARGE, voteCount = 132_000)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
          TMDBRatingItem(rating = 5.4, size = RatingSize.SMALL, voteCount = null)
          TMDBRatingItem(rating = 5.0, size = RatingSize.MEDIUM, voteCount = null)
          TMDBRatingItem(rating = 5.0, size = RatingSize.LARGE, voteCount = null)
          TMDBRatingItem(rating = 5.4, size = RatingSize.SMALL, voteCount = 132_583)
          TMDBRatingItem(rating = 5.0, size = RatingSize.MEDIUM, voteCount = 132_583)
          TMDBRatingItem(rating = 5.0, size = RatingSize.LARGE, voteCount = 132_583)
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          TMDBRatingItem(rating = 1.1, size = RatingSize.LARGE, voteCount = 932_583)
          TMDBRatingItem(rating = 1.1, size = RatingSize.LARGE, voteCount = 1_432_583)
          TMDBRatingItem(rating = 5.9, size = RatingSize.LARGE, voteCount = 2_992_583)
        }
        Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          TMDBRatingItem(rating = 9.2, size = RatingSize.LARGE, voteCount = 1_202_583)
          TMDBRatingItem(rating = 9.2, size = RatingSize.LARGE, voteCount = 1_102_583)
        }
      }
    }
  }
}
