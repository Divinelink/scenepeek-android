package com.divinelink.feature.tmdb.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation

fun NavGraphBuilder.tmdbAuthScreen(up: () -> Unit) {
  composable<Navigation.TMDBAuthRoute> {
    TMDBAuthScreen(up)
  }
}
