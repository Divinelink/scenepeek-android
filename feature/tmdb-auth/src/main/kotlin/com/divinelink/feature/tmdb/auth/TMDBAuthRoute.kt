package com.divinelink.feature.tmdb.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object TMDBAuthRoute

fun NavController.navigateToTMDBAuth() = navigate(route = TMDBAuthRoute)

fun NavGraphBuilder.tmdbAuthScreen(up: () -> Unit) {
  composable<TMDBAuthRoute> {
    TMDBAuthScreen(up)
  }
}
