@file:Suppress("MagicNumber")

package com.divinelink.core.ui.rating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_metascore
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.stringResource

@Composable
fun MetascoreRatingItem(
  modifier: Modifier = Modifier,
  ratingDetails: RatingDetails,
) {
  Row(
    modifier = modifier.testTag(TestTags.Rating.METACRITIC_RATING),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    when (ratingDetails) {
      RatingDetails.Initial -> MetascoreRatingItemSkeleton()
      RatingDetails.Unavailable -> Text(
        text = "-",
        style = MaterialTheme.typography.labelLarge,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
      )
      is RatingDetails.Score -> Unit
      is RatingDetails.Rating -> {
        Row(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          MetascoreBox(score = ratingDetails.value)
          Text(
            text = stringResource(UiString.core_ui_metascore),
            style = MaterialTheme.typography.bodyMedium,
          )
        }
      }
    }
  }
}

@Composable
private fun MetascoreBox(
  score: Int,
  modifier: Modifier = Modifier,
) {
  val backgroundColor = when (score) {
    in 0..39 -> Color(0xFFE53935) // Red
    in 40..60 -> Color(0xFFFB8C00) // Orange
    in 61..100 -> MaterialTheme.colors.emeraldGreen
    else -> Color.Gray
  }

  Box(
    modifier = modifier
      .size(MaterialTheme.dimensions.keyline_40)
      .background(
        color = backgroundColor,
        shape = MaterialTheme.shape.medium,
      ),
    contentAlignment = Alignment.Center,
  ) {
    CompositionLocalProvider(
      LocalDensity provides Density(
        density = LocalDensity.current.density,
        fontScale = LocalDensity.current.fontScale.coerceIn(1f, 1.35f),
      ),
    ) {
      Text(
        text = score.toString(),
        color = Color.White,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
      )
    }
  }
}

@Composable
private fun MetascoreRatingItemSkeleton(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.testTag(TestTags.Rating.METACRITIC_RATING_SKELETON),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = " ",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_96)
        .height(MaterialTheme.dimensions.keyline_40)
        .shimmer()
        .clip(MaterialTheme.shapes.extraLarge)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  }
}

@Previews
@Composable
fun MetascoreRatingItemPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        MetascoreRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Rating(77),
        )
        MetascoreRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Rating(61),
        )
        MetascoreRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Rating(60),
        )
        MetascoreRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Rating(39),
        )
        MetascoreRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Initial,
        )
        MetascoreRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Unavailable,
        )
      }
    }
  }
}
