package com.divinelink.feature.tmdb.auth

import androidx.navigation3.runtime.EntryProviderScope
import com.divinelink.core.navigation.route.Navigation

fun EntryProviderScope<Navigation>.tmdbAuthScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.TMDBAuthRoute> { _ ->
    TMDBAuthScreen(onNavigate)
  }
}
