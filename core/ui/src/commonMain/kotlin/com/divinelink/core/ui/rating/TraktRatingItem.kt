@file:Suppress("MagicNumber")

package com.divinelink.core.ui.rating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.extensions.toShortString
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.valentinilk.shimmer.shimmer

@Composable
fun TraktRatingItem(
  modifier: Modifier = Modifier,
  ratingDetails: RatingDetails,
) {
  Row(
    modifier = Modifier.testTag(TestTags.Rating.TRAKT_RATING),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Icon(
      modifier = Modifier.size(MaterialTheme.dimensions.keyline_32),
      imageVector = Icons.Default.Favorite,
      tint = Color(red = 168, green = 25, blue = 24),
      contentDescription = null,
    )

    when (ratingDetails) {
      RatingDetails.Initial -> TraktRatingContentShimmer()
      RatingDetails.Unavailable -> Text(
        text = "-",
        style = MaterialTheme.typography.labelLarge,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
      )
      is RatingDetails.Score -> Column {
        Row(
          modifier = modifier,
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "${ratingDetails.voteAverage.times(10).toInt()}%",
            style = MaterialTheme.typography.labelLarge,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
          )
        }

        Text(
          text = (ratingDetails.voteCount.toShortString() + " votes"),
          style = MaterialTheme.typography.labelSmall,
          fontSize = MaterialTheme.typography.titleSmall.fontSize,
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
        )
      }
    }
  }
}

@Composable
private fun TraktRatingContentShimmer(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.testTag(TestTags.Rating.TRAKT_RATING_SKELETON),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = " ",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_48)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )

    Text(
      text = " ",
      style = MaterialTheme.typography.titleSmall,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_84)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  }
}

@Previews
@Composable
fun TraktRatingItemPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        TraktRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Score(
            voteAverage = 7.7,
            voteCount = 2_345,
          ),
        )
        TraktRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Initial,
        )
        TraktRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Unavailable,
        )
      }
    }
  }
}
