package com.divinelink.feature.updater.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.updater.ui.UpdaterScreen

fun NavGraphBuilder.updaterScreen(onNavigate: (Navigation) -> Unit) {
  dialog<Navigation.UpdaterRoute> {
    UpdaterScreen(
      onNavigate = onNavigate,
    )
  }
}
