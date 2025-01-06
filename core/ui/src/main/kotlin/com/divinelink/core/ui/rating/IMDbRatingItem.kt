@file:Suppress("MagicNumber")

package com.divinelink.core.ui.rating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.divinelink.core.commons.extensions.toShortString
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.ui.Previews
import com.valentinilk.shimmer.shimmer

@Composable
fun IMDbRatingItem(
  modifier: Modifier = Modifier,
  ratingDetails: RatingDetails,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Icon(
      modifier = Modifier.size(MaterialTheme.dimensions.keyline_36),
      imageVector = Icons.Rounded.StarRate,
      tint = Color(0xFFE6C229),
      contentDescription = null,
    )

    when (ratingDetails) {
      RatingDetails.Initial -> IMDbRatingContentShimmer()
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
            text = ratingDetails.voteAverage.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
          )

          Text(
            text = " / 10",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
          )
        }

        Text(
          text = ratingDetails.voteCount.toShortString(),
          style = MaterialTheme.typography.labelSmall,
          fontSize = MaterialTheme.typography.titleSmall.fontSize,
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
        )
      }
    }
  }
}

@Composable
private fun IMDbRatingContentShimmer(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = " ",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_58)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )

    Text(
      text = " ",
      style = MaterialTheme.typography.titleSmall,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_32)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  }
}

@Previews
@Composable
fun IMDbRatingItemPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        IMDbRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Score(
            voteAverage = 8.5,
            voteCount = 2_345,
          ),
        )
        IMDbRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Initial,
        )

        IMDbRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Unavailable,
        )
      }
    }
  }
}
