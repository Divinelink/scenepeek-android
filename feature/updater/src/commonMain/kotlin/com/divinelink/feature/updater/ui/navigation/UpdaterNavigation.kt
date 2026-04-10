package com.divinelink.feature.updater.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.updater.ui.UpdaterScreen

fun EntryProviderScope<Navigation>.updaterScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.UpdaterRoute>(metadata = DialogSceneStrategy.dialog()) { _ ->
    UpdaterScreen(
      onNavigate = onNavigate,
    )
  }
}
