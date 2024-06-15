package com.andreolas.movierama.navigation

import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.generated.watchlist.destinations.WatchlistScreenDestination

@NavHostGraph
annotation class MainGraph {
  @ExternalDestination<WatchlistScreenDestination>
  companion object Includes
}

@NavGraph<MainGraph>
annotation class SettingsGraph
