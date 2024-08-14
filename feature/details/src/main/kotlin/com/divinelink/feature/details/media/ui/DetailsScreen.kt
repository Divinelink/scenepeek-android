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
import androidx.compose.ui.platform.testTag
import com.divinelink.core.navigation.arguments.CreditsNavArguments
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.map
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.details.media.ui.rate.RateModalBottomSheet
import com.divinelink.feature.details.navigation.details.MediaDetailsGraph
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.parameters.DeepLink
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private const val BOTTOM_SHEET_DELAY = 200L

@OptIn(ExperimentalMaterial3Api::class)
@Destination<MediaDetailsGraph>(
  start = true,
  navArgs = DetailsNavArguments::class,
  deepLinks = [
    DeepLink(uriPattern = "https://www.themoviedb.org/{mediaType}/{id}-.*"),
    DeepLink(uriPattern = "https://www.themoviedb.org/{mediaType}/{id}"),
  ],
)
@Composable
fun DetailsScreen(
  navigator: DestinationsNavigator,
  viewModel: DetailsViewModel = koinViewModel(),
  onNavigateToCredits: (CreditsNavArguments) -> Unit,
  onNavigateToAccountSettings: () -> Unit,
) {
  val viewState = viewModel.viewState.collectAsState()
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }

  LaunchedEffect(viewState.value.navigateToLogin) {
    viewState.value.navigateToLogin?.let {
      onNavigateToAccountSettings()

      viewModel.consumeNavigateToLogin()
    }
  }
  val rateBottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
  )

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

  DetailsContent(
    viewState = viewState.value,
    onNavigateUp = navigator::popBackStack,
    onMarkAsFavoriteClicked = viewModel::onMarkAsFavorite,
    onSimilarMovieClicked = { movie ->
      val navArgs = DetailsNavArguments(
        id = movie.id,
        mediaType = movie.mediaType.value,
        isFavorite = movie.isFavorite ?: false,
      )
      val destination = DetailsScreenDestination(navArgs = navArgs)

      navigator.navigate(destination)
    },
    onPersonClick = { person -> navigator.navigate(PersonScreenDestination(person.map())) },
    onConsumeSnackbar = viewModel::consumeSnackbarMessage,
    onAddRateClicked = viewModel::onAddRateClicked,
    onAddToWatchlistClicked = viewModel::onAddToWatchlist,
    requestMedia = viewModel::onRequestMedia,
    viewAllCreditsClicked = {
      onNavigateToCredits(
        CreditsNavArguments(
          mediaType = viewState.value.mediaType,
          id = viewState.value.mediaDetails?.id?.toLong()!!,
        ),
      )
    },
  )
}
