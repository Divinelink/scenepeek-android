package com.divinelink.feature.details.media.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.util.launchCustomTab
import com.divinelink.core.navigation.route.CreditsRoute
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.core.navigation.route.map
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.OverlayScreen
import com.divinelink.core.ui.components.details.videos.YouTubePlayerScreen
import com.divinelink.feature.details.media.ui.rate.RateModalBottomSheet
import com.divinelink.feature.details.media.ui.ratings.AllRatingsModalBottomSheet
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private const val BOTTOM_SHEET_DELAY = 200L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToCredits: (CreditsRoute) -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: DetailsViewModel = koinViewModel(),
) {
  var videoUrl by rememberSaveable { mutableStateOf<String?>(null) }

  val viewState by viewModel.viewState.collectAsStateWithLifecycle()
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  var showAllRatingBottomSheet by rememberSaveable { mutableStateOf(false) }
  val context = LocalContext.current

  BackHandler {
    if (videoUrl.isNullOrEmpty()) {
      onNavigateUp()
    } else {
      videoUrl = null
    }
  }

  LaunchedEffect(viewState.navigateToLogin) {
    viewState.navigateToLogin?.let {
      onNavigateToTMDBLogin()

      viewModel.consumeNavigateToLogin()
    }
  }
  val rateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val allRatingsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  LaunchedEffect(viewState.showRateDialog) {
    if (viewState.showRateDialog) {
      openBottomSheet = true
      delay(BOTTOM_SHEET_DELAY)
      rateBottomSheetState.show()
    } else {
      rateBottomSheetState.hide()
      openBottomSheet = false
    }
  }

  LaunchedEffect(showAllRatingBottomSheet) {
    if (showAllRatingBottomSheet) {
      allRatingsBottomSheetState.show()
    } else {
      allRatingsBottomSheetState.hide()
    }
  }

  LaunchedEffect(Unit) {
    viewModel.openUrlTab.collect { url ->
      launchCustomTab(context, url)
    }
  }

  if (openBottomSheet) {
    RateModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Details.RATE_DIALOG),
      sheetState = rateBottomSheetState,
      value = viewState.userDetails?.beautifiedRating,
      mediaTitle = viewState.mediaDetails?.title ?: "",
      onSubmitRate = viewModel::onSubmitRate,
      onClearRate = viewModel::onClearRating,
      onRateChanged = {
        // TODO implement
      },
      onDismissRequest = viewModel::onDismissRateDialog,
      canClearRate = viewState.userDetails?.rating != null,
    )
  }

  if (showAllRatingBottomSheet) {
    viewState.mediaDetails?.ratingCount?.let { ratingCount ->
      AllRatingsModalBottomSheet(
        sheetState = allRatingsBottomSheetState,
        onDismissRequest = { showAllRatingBottomSheet = false },
        ratingCount = ratingCount,
        onClick = viewModel::onMediaSourceClick,
      )
    }
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize(),
  ) {
    DetailsContent(
      viewState = viewState,
      onNavigateUp = onNavigateUp,
      onMarkAsFavoriteClicked = viewModel::onMarkAsFavorite,
      onSimilarMovieClicked = { movie ->
        val navArgs = DetailsRoute(
          id = movie.id,
          mediaType = movie.mediaType,
          isFavorite = movie.isFavorite ?: false,
        )
        onNavigateToDetails(navArgs)
      },
      onPersonClick = { person -> onNavigateToPerson(person.map()) },
      onConsumeSnackbar = viewModel::consumeSnackbarMessage,
      onAddRateClick = viewModel::onAddRateClicked,
      onAddToWatchlistClick = viewModel::onAddToWatchlist,
      requestMedia = viewModel::onRequestMedia,
      onObfuscateSpoilers = viewModel::onObfuscateSpoilers,
      onViewAllCreditsClick = {
        viewState.mediaDetails?.id?.let { id ->
          onNavigateToCredits(
            CreditsRoute(
              mediaType = viewState.mediaType,
              id = id.toLong(),
            ),
          )
        }
      },
      onShowAllRatingsClick = {
        showAllRatingBottomSheet = true
        viewModel.onFetchAllRatings()
      },
      onTabSelected = viewModel::onTabSelected,
      onPlayTrailerClick = { videoUrl = it },
      animatedVisibilityScope = animatedVisibilityScope,
    )

    OverlayScreen(
      isVisible = !videoUrl.isNullOrEmpty(),
      onDismiss = { videoUrl = null },
      content = {
        videoUrl?.let {
          YouTubePlayerScreen(
            videoId = it,
            onBack = { videoUrl = null },
          )
        }
      },
    )
  }
}
