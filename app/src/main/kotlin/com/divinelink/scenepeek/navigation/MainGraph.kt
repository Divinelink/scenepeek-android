package com.divinelink.scenepeek.navigation

import com.divinelink.feature.credits.screens.destinations.CreditsScreenDestination
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import com.divinelink.feature.settings.screens.navgraphs.SettingsNavGraph
import com.divinelink.ui.screens.destinations.WatchlistScreenDestination
import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph

@NavHostGraph
annotation class MainGraph {
  @ExternalDestination<DetailsScreenDestination>
  @ExternalDestination<PersonScreenDestination>
  @ExternalDestination<WatchlistScreenDestination>
  @ExternalDestination<CreditsScreenDestination>
  @ExternalNavGraph<SettingsNavGraph>
  companion object Includes
}
