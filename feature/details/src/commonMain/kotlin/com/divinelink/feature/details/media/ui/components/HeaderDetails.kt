package com.divinelink.feature.details.media.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.WatchTrailerButton
import com.divinelink.core.ui.rating.MediaRatingItem
import com.divinelink.feature.details.media.ui.DetailsViewState

@Composable
fun HeaderDetails(
  mediaDetails: MediaDetails,
  uiState: DetailsViewState,
  onOpenManageModal: () -> Unit,
  trailer: Video?,
  onWatchTrailer: (String) -> Unit,
  viewAllRatingsClick: () -> Unit,
) {
  Column {
    TitleDetails(mediaDetails = mediaDetails)
    AnimatedVisibility(uiState.jellyseerrMediaInfo?.status != null) {
      uiState.jellyseerrMediaInfo?.status?.let { status ->
        JellyseerrStatusPill(
          modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8),
          status = status,
          onClick = if (uiState.canManageRequests) {
            { onOpenManageModal() }
          } else {
            null
          },
        )
      }
    }

    AnimatedVisibility(uiState.trailer != null) {
      WatchTrailerButton(
        modifier = Modifier.offset(x = -MaterialTheme.dimensions.keyline_12),
        onClick = { trailer?.key?.let { onWatchTrailer(it) } },
      )
    }
    TextButton(
      modifier = Modifier
        .offset(
          y = -MaterialTheme.dimensions.keyline_4,
          x = -MaterialTheme.dimensions.keyline_12,
        )
        .testTag(TestTags.Rating.DETAILS_RATING_BUTTON),
      onClick = viewAllRatingsClick,
    ) {
      MediaRatingItem(
        ratingDetails = mediaDetails.ratingCount.getRatingDetails(
          uiState.ratingSource,
        ),
        source = uiState.ratingSource,
      )
    }
  }
}
