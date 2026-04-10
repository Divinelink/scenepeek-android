package com.divinelink.feature.details.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.details.poster.PosterScreen

fun EntryProviderScope<Navigation>.posterScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.MediaPosterRoute> { key ->
    LocalNavAnimatedContentScope.current.PosterScreen(
      path = key.posterPath,
      onNavigate = onNavigate,
    )
  }
}
