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
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.CreditsRoute
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.TMDBAuthRoute
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.OverlayScreen
import com.divinelink.core.ui.components.details.videos.YouTubePlayerScreen
import com.divinelink.feature.details.media.ui.rate.RateModalBottomSheet
import com.divinelink.feature.details.media.ui.ratings.AllRatingsModalBottomSheet
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: DetailsViewModel = koinViewModel(),
) {
  var videoUrl by rememberSaveable { mutableStateOf<String?>(null) }

  val viewState by viewModel.viewState.collectAsStateWithLifecycle()
  var showAllRatingBottomSheet by rememberSaveable { mutableStateOf(false) }
  var openRateBottomSheet by rememberSaveable { mutableStateOf(false) }
  val context = LocalContext.current

  BackHandler {
    if (videoUrl.isNullOrEmpty()) {
      onNavigate(Navigation.Back)
    } else {
      videoUrl = null
    }
  }

  LaunchedEffect(viewState.navigateToLogin) {
    viewState.navigateToLogin?.let {
      onNavigate(TMDBAuthRoute)

      viewModel.consumeNavigateToLogin()
    }
  }
  val rateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val allRatingsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  LaunchedEffect(Unit) {
    viewModel.openUrlTab.collect { url ->
      launchCustomTab(
        context = context,
        url = url,
        webViewFallback = {
          onNavigate(
            Navigation.WebViewRoute(
              url = url,
              title = viewState.mediaDetails?.title ?: "",
            ),
          )
        },
      )
    }
  }

  if (openRateBottomSheet) {
    RateModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Details.RATE_DIALOG),
      sheetState = rateBottomSheetState,
      value = viewState.userDetails.beautifiedRating,
      mediaTitle = viewState.mediaDetails?.title ?: "",
      onSubmitRate = {
        openRateBottomSheet = false
        viewModel.onSubmitRate(it)
      },
      onClearRate = viewModel::onClearRating,
      onDismissRequest = { openRateBottomSheet = false },
      canClearRate = viewState.userDetails.rating != null,
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
      onNavigate = onNavigate,
      animatedVisibilityScope = animatedVisibilityScope,
      onMarkAsFavoriteClicked = viewModel::onMarkAsFavorite,
      onSimilarMovieClicked = { movie ->
        val route = DetailsRoute(
          id = movie.id,
          mediaType = movie.mediaType,
          isFavorite = movie.isFavorite ?: false,
        )
        onNavigate(route)
      },
      onPersonClick = { person -> onNavigate(person.toPersonRoute()) },
      onConsumeSnackbar = viewModel::consumeSnackbarMessage,
      onAddRateClick = { openRateBottomSheet = true },
      onAddToWatchlistClick = viewModel::onAddToWatchlist,
      onObfuscateSpoilers = viewModel::onObfuscateSpoilers,
      onViewAllCreditsClick = {
        viewState.mediaDetails?.id?.let { id ->
          onNavigate(
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
      onDeleteRequest = viewModel::onDeleteRequest,
      onDeleteMedia = viewModel::onDeleteMedia,
      onUpdateMediaInfo = viewModel::onUpdateMediaInfo,
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
