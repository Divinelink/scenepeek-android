package com.divinelink.feature.details.media.ui

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.CreditsRoute
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.TMDBAuthRoute
import com.divinelink.core.navigation.route.toPersonRoute
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.videos.YouTubePlayerScreen
import com.divinelink.core.ui.manager.url.rememberUrlHandler
import com.divinelink.feature.details.media.ui.rate.RateModalBottomSheet
import com.divinelink.feature.details.media.ui.ratings.AllRatingsModalBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DetailsScreen(
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: DetailsViewModel = koinViewModel(),
  switchViewButtonViewModel: SwitchViewButtonViewModel = koinViewModel(),
) {
  var videoUrl by rememberSaveable { mutableStateOf<String?>(null) }

  val viewState by viewModel.viewState.collectAsStateWithLifecycle()
  val urlHandler = rememberUrlHandler()
  var showAllRatingBottomSheet by rememberSaveable { mutableStateOf(false) }
  var openRateBottomSheet by rememberSaveable { mutableStateOf(false) }

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
      urlHandler.openUrl(
        url = url,
        onError = {
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
      onMediaItemClick = { media ->
        val route = DetailsRoute(
          id = media.id,
          mediaType = media.mediaType.value,
          isFavorite = (media as? MediaItem.Media)?.isFavorite,
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
              mediaType = viewState.mediaType.value,
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
      onSwitchViewMode = switchViewButtonViewModel::switchViewMode,
    )

    videoUrl?.let {
      YouTubePlayerScreen(
        videoId = it,
        onBack = { videoUrl = null },
      )
    }
  }
}
