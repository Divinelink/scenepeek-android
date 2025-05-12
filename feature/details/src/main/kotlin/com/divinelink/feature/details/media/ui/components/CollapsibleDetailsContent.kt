package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.WatchlistButton
import com.divinelink.core.ui.nestedscroll.CollapsingContentNestedScrollConnection
import com.divinelink.feature.details.media.ui.rate.UserRating

private const val OVERVIEW_WEIGHT = 3f

@Composable
fun CollapsibleDetailsContent(
  modifier: Modifier = Modifier,
  connection: CollapsingContentNestedScrollConnection,
  mediaDetails: MediaDetails,
  isOnWatchlist: Boolean,
  userDetails: AccountMediaDetails?,
  ratingSource: RatingSource,
  onAddToWatchListClick: () -> Unit,
  onAddRateClick: () -> Unit,
  onViewAllRatingsClick: () -> Unit,
) {
  Column(
    modifier = modifier
      .verticalScroll(state = rememberScrollState())
      .testTag(TestTags.Details.COLLAPSIBLE_CONTENT)
      .fillMaxWidth()
      .graphicsLayer {
        translationY = -(connection.maxHeight.toPx() - connection.currentSize.toPx())
      },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    TitleDetails(mediaDetails)

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues = ListPaddingValues),
    ) {
      MovieImage(
        modifier = Modifier.weight(1f),
        path = mediaDetails.posterPath,
      )

      OverviewDetails(
        modifier = Modifier.weight(OVERVIEW_WEIGHT),
        movieDetails = mediaDetails,
        genres = mediaDetails.genres,
        onGenreClicked = {},
      )
    }

    WatchlistButton(
      modifier = Modifier.padding(paddingValues = ListPaddingValues),
      onWatchlist = isOnWatchlist,
      onClick = onAddToWatchListClick,
    )

    UserRating(
      ratingDetails = mediaDetails.ratingCount.getRatingDetails(ratingSource),
      accountRating = userDetails?.beautifiedRating,
      onAddRateClicked = onAddRateClick,
      onShowAllRatingsClicked = onViewAllRatingsClick,
      source = ratingSource,
    )
  }
}
