package com.divinelink.feature.details.media.ui.collapsing

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.collapsing.CollapsingOption
import com.divinelink.core.ui.collapsing.CollapsingToolBarLayout
import com.divinelink.core.ui.collapsing.rememberCollapsingToolBarState
import com.divinelink.feature.details.media.ui.components.CollapsibleDetailsContent

@Composable
fun DynamicDetailsCollapsingToolbar(
  mediaDetails: MediaDetails,
  ratingSource: RatingSource,
  onAddToWatchlistClicked: () -> Unit,
  userDetails: AccountMediaDetails?,
  onAddRateClicked: () -> Unit,
  viewAllRatingsClicked: () -> Unit,
  onShowTitle: (Boolean) -> Unit,
  content: @Composable () -> Unit,
) {
  SubcomposeLayout { constraints ->
    // 1. Measure the toolbar
    val toolbarPlaceable = subcompose("toolbar") {
      CollapsibleDetailsContent(
        modifier = Modifier.fillMaxWidth(),
        mediaDetails = mediaDetails,
        isOnWatchlist = userDetails?.watchlist == true,
        userDetails = userDetails,
        ratingSource = ratingSource,
        ratingCount = mediaDetails.ratingCount,
        onAddToWatchListClick = onAddToWatchlistClicked,
        onAddRateClick = onAddRateClicked,
        onShowAllRatingsClick = viewAllRatingsClicked,
      )
    }.first().measure(constraints)

    val toolbarHeight = toolbarPlaceable.height.toDp()

    val collapsingToolbarPlaceable = subcompose("content") {
      val state = rememberCollapsingToolBarState(
        toolBarMaxHeight = toolbarHeight,
        toolBarMinHeight = 0.dp,
        collapsingOption = CollapsingOption.EnterAlwaysCollapsed,
      )

      LaunchedEffect(state.progress) {
        onShowTitle(state.progress > 0.22f)
      }

      CollapsingToolBarLayout(
        state = state,
        modifier = Modifier
          .testTag(TestTags.Details.CONTENT_SCAFFOLD)
          .navigationBarsPadding(),
        toolbar = {
          CollapsibleDetailsContent(
            modifier = Modifier
              .requiredToolBarMaxHeight()
              .fillMaxWidth(),
            mediaDetails = mediaDetails,
            isOnWatchlist = userDetails?.watchlist == true,
            userDetails = userDetails,
            ratingSource = ratingSource,
            ratingCount = mediaDetails.ratingCount,
            onAddToWatchListClick = onAddToWatchlistClicked,
            onAddRateClick = onAddRateClicked,
            onShowAllRatingsClick = viewAllRatingsClicked,
          )
        },
        content = { content() },
      )
    }.first().measure(constraints)

    layout(constraints.maxWidth, constraints.maxHeight) {
      collapsingToolbarPlaceable.place(0, 0)
    }
  }
}
