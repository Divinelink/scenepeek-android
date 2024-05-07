package com.andreolas.movierama.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.destinations.AccountSettingsScreenDestination
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
  navArgsDelegate = DetailsNavArguments::class,
)
@Composable
fun DetailsScreen(
  navigator: DestinationsNavigator,
  viewModel: DetailsViewModel = hiltViewModel(),
) {
  val viewState = viewModel.viewState.collectAsState()

  LaunchedEffect(viewState.value.navigateToLogin) {
    viewState.value.navigateToLogin?.let {
      navigator.navigate(AccountSettingsScreenDestination)

      viewModel.consumeNavigateToLogin()
    }
  }

  DetailsContent(
    viewState = viewState.value,
    onNavigateUp = navigator::popBackStack,
    onMarkAsFavoriteClicked = viewModel::onMarkAsFavorite,
    onSubmitRate = viewModel::onSubmitRate,
    onSimilarMovieClicked = { movie ->
      val navArgs = DetailsNavArguments(
        id = movie.id,
        mediaType = movie.mediaType.value,
        isFavorite = movie.isFavorite ?: false,
      )
      val destination = DetailsScreenDestination(
        navArgs = navArgs,
      )

      navigator.navigate(destination)
    },
    onConsumeSnackbar = viewModel::consumeSnackbarMessage,
    onAddRateClicked = viewModel::onAddRateClicked,
    onDismissBottomSheet = viewModel::onDismissBottomSheet,
  )
}
