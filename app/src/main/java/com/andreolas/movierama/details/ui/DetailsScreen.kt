package com.andreolas.movierama.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    navArgsDelegate = DetailsNavArguments::class,
)
@Composable
@Suppress("UnusedPrivateMember")
fun DetailsScreen(
    navigator: DestinationsNavigator,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val viewState = viewModel.viewState.collectAsState()
}
