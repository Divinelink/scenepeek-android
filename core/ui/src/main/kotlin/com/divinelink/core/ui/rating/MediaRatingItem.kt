package com.divinelink.core.ui.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.ui.R

@Composable
fun MediaRatingItem(
  modifier: Modifier = Modifier,
  ratingDetails: RatingDetails,
  source: RatingSource,
) {
  when (source) {
    RatingSource.TMDB -> Row(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      TMDBRatingItem(
        modifier = modifier,
        rating = (ratingDetails as? RatingDetails.Score)?.voteAverage,
        voteCount = (ratingDetails as? RatingDetails.Score)?.voteCount,
        size = RatingSize.LARGE,
      )
      Text(
        text = stringResource(id = R.string.core_ui_tmdb_user_score),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
      )
    }
    RatingSource.IMDB -> IMDbRatingItem(
      modifier = modifier,
      ratingDetails = ratingDetails,
    )
    RatingSource.TRAKT -> TraktRatingItem(
      modifier = modifier,
      ratingDetails = ratingDetails,
    )
  }
}
