package com.andreolas.movierama.navigation

import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.settings.screens.navgraphs.SettingsNavGraph
import com.divinelink.ui.screens.destinations.WatchlistScreenDestination
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph

@NavHostGraph
annotation class MainGraph {
  @ExternalDestination<DetailsScreenDestination>
  @ExternalDestination<WatchlistScreenDestination>
  @ExternalNavGraph<SettingsNavGraph>
  companion object Includes
}
