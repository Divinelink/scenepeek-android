package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.WatchlistButton
import com.divinelink.core.ui.extension.getColorRating
import com.divinelink.core.ui.nestedscroll.CollapsingContentNestedScrollConnection
import com.divinelink.feature.details.R

@Composable
fun CollapsibleDetailsContent(
  modifier: Modifier = Modifier,
  connection: CollapsingContentNestedScrollConnection,
  mediaDetails: MediaDetails,
  isOnWatchlist: Boolean,
  userDetails: AccountMediaDetails?,
  onAddToWatchListClick: () -> Unit,
  onAddRateClick: () -> Unit,
) {
  Column(
    modifier = modifier
      .verticalScroll(state = rememberScrollState())
      .testTag(TestTags.Details.COLLAPSIBLE_CONTENT)
      .padding(MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth()
      .graphicsLayer {
        translationY = -(connection.maxHeight.toPx() - connection.currentSize.toPx())
      },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    ) {
      MovieImage(
        modifier = Modifier.weight(1.35f),
        path = mediaDetails.posterPath,
      )

      TitleDetails(
        modifier = Modifier
          .weight(3f)
          .padding(top = MaterialTheme.dimensions.keyline_56),
        mediaDetails = mediaDetails,
      )
    }

    Row(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      RatingButton(
        modifier = Modifier.weight(3f),
        onClick = onAddRateClick,
        accountRating = userDetails?.beautifiedRating,
      )

      WatchlistButton(
        modifier = Modifier.weight(2f),
        onWatchlist = isOnWatchlist,
        onClick = onAddToWatchListClick,
      )
    }
  }
}

@Composable
fun RatingButton(
  modifier: Modifier = Modifier,
  accountRating: Int?,
  onClick: () -> Unit,
) {
  ElevatedButton(
    shape = MaterialTheme.shape.large,
    elevation = ButtonDefaults.buttonElevation(
      defaultElevation = MaterialTheme.dimensions.keyline_2,
    ),
    modifier = modifier,
    onClick = onClick,
  ) {
    if (accountRating != null) {
      val color = accountRating.toDouble().getColorRating()

      DetailsRatingText(modifier, accountRating, color)
    } else {
      Text(
        text = stringResource(id = R.string.details__add_rating),
        style = MaterialTheme.typography.titleSmall,
      )
    }
  }
}

@Composable
private fun DetailsRatingText(
  modifier: Modifier,
  accountRating: Int,
  color: Color,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = stringResource(id = R.string.details__your_rating),
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.titleSmall,
      textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))

    Text(
      modifier = Modifier.testTag(TestTags.Details.YOUR_RATING),
      text = accountRating.toString(),
      color = color,
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center,
    )
  }
}
