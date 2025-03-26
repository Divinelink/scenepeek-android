package com.divinelink.feature.details.media.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.util.launchCustomTab
import com.divinelink.core.navigation.route.CreditsRoute
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.core.navigation.route.map
import com.divinelink.core.ui.TestTags
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
  viewModel: DetailsViewModel = koinViewModel(),
) {
  val viewState = viewModel.viewState.collectAsState()
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  var showAllRatingBottomSheet by rememberSaveable { mutableStateOf(false) }
  val context = LocalContext.current

  LaunchedEffect(viewState.value.navigateToLogin) {
    viewState.value.navigateToLogin?.let {
      onNavigateToTMDBLogin()

      viewModel.consumeNavigateToLogin()
    }
  }
  val rateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val allRatingsBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  LaunchedEffect(viewState.value.showRateDialog) {
    if (viewState.value.showRateDialog) {
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
      value = viewState.value.userDetails?.beautifiedRating,
      mediaTitle = viewState.value.mediaDetails?.title ?: "",
      onSubmitRate = viewModel::onSubmitRate,
      onClearRate = viewModel::onClearRating,
      onRateChanged = {
        // TODO implement
      },
      onDismissRequest = viewModel::onDismissRateDialog,
      canClearRate = viewState.value.userDetails?.rating != null,
    )
  }

  if (showAllRatingBottomSheet) {
    viewState.value.mediaDetails?.ratingCount?.let { ratingCount ->
      AllRatingsModalBottomSheet(
        sheetState = allRatingsBottomSheetState,
        onDismissRequest = { showAllRatingBottomSheet = false },
        ratingCount = ratingCount,
        onClick = viewModel::onMediaSourceClick,
      )
    }
  }

  DetailsContent(
    viewState = viewState.value,
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
    onAddRateClicked = viewModel::onAddRateClicked,
    onAddToWatchlistClicked = viewModel::onAddToWatchlist,
    requestMedia = viewModel::onRequestMedia,
    onObfuscateSpoilers = viewModel::onObfuscateSpoilers,
    viewAllCreditsClicked = {
      viewState.value.mediaDetails?.id?.let { id ->
        onNavigateToCredits(
          CreditsRoute(
            mediaType = viewState.value.mediaType,
            id = id.toLong(),
          ),
        )
      }
    },
    viewAllRatingsClicked = {
      showAllRatingBottomSheet = true
      viewModel.onFetchAllRatings()
    },
  )
}
