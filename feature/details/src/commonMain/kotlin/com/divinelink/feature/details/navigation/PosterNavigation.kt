package com.divinelink.feature.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.details.poster.PosterScreen

fun NavGraphBuilder.posterScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.MediaPosterRoute> {
    val route = it.toRoute<Navigation.MediaPosterRoute>()

    PosterScreen(
      path = route.posterPath,
      onNavigate = onNavigate,
    )
  }
}
