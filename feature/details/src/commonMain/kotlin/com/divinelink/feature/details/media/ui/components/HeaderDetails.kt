package com.divinelink.feature.details.media.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.PosterImage
import com.divinelink.core.ui.components.JellyseerrStatusPill
import com.divinelink.core.ui.components.WatchTrailerButton
import com.divinelink.core.ui.mediaImageDropShadow
import com.divinelink.core.ui.rating.MediaRatingItem
import com.divinelink.feature.details.media.ui.DetailsViewState

@Composable
fun SharedTransitionScope.HeaderDetails(
  mediaDetails: MediaDetails,
  visibilityScope: AnimatedVisibilityScope,
  onNavigate: (Navigation) -> Unit,
  uiState: DetailsViewState,
  onOpenManageModal: () -> Unit,
  trailer: Video?,
  onWatchTrailer: (String) -> Unit,
  viewAllRatingsClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    PosterImage(
      modifier = Modifier
        .sharedElement(
          sharedContentState = rememberSharedContentState(
            SharedElementKeys.MediaPoster(mediaDetails.posterPath),
          ),
          animatedVisibilityScope = visibilityScope,
        )
        .mediaImageDropShadow()
        .height(MaterialTheme.dimensions.posterSizeSmall)
        .aspectRatio(2f / 3f),
      path = mediaDetails.posterPath,
      quality = ImageQuality.QUALITY_342,
      onClick = { onNavigate(Navigation.MediaPosterRoute(it)) },
    )

    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceEvenly,
    ) {
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
}
