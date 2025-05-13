package com.divinelink.feature.details.media.ui.rate

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.SpannableRating
import com.divinelink.core.ui.rating.MediaRatingItem
import com.divinelink.feature.details.R

@Composable
fun UserRating(
  modifier: Modifier = Modifier,
  source: RatingSource,
  ratingDetails: RatingDetails,
  accountRating: Int?,
  onAddRateClicked: () -> Unit,
  onShowAllRatingsClicked: () -> Unit,
) {
  Row(
    modifier = modifier.fillMaxSize(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_12),
    )

    TextButton(
      modifier = Modifier.testTag(TestTags.Rating.DETAILS_RATING_BUTTON),
      onClick = onShowAllRatingsClicked,
    ) {
      MediaRatingItem(
        ratingDetails = ratingDetails,
        source = source,
      )
    }

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_0),
    )

    TextButton(
      modifier = Modifier.align(Alignment.CenterVertically),
      onClick = onAddRateClicked,
    ) {
      if (accountRating != null) {
        SpannableRating(
          modifier = Modifier.align(Alignment.CenterVertically),
          text = stringResource(id = R.string.details__your_rating),
          rating = accountRating,
        )
      } else {
        Text(
          text = stringResource(id = R.string.details__add_rating),
          style = MaterialTheme.typography.titleMedium,
        )
      }
    }

    Spacer(
      modifier = Modifier
        .weight(1f)
        .size(MaterialTheme.dimensions.keyline_24),
    )
  }
}
