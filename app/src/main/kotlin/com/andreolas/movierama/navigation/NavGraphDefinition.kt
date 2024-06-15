package com.andreolas.movierama.navigation

import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.ui.screens.destinations.WatchlistScreenDestination
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph

@NavHostGraph
annotation class MainGraph {
  @ExternalDestination<DetailsScreenDestination>
  @ExternalDestination<WatchlistScreenDestination>
  companion object Includes
}

@NavGraph<MainGraph>
annotation class SettingsGraph
