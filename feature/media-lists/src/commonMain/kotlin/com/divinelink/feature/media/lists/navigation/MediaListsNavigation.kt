package com.divinelink.feature.media.lists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.NavType
import com.divinelink.feature.media.lists.ui.MediaListsScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.mediaListsScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.MediaListsRoute>(
    typeMap = mapOf(typeOf<HomeSection>() to NavType.HomeSection),
  ) {
    MediaListsScreen(
      onNavigate = onNavigate,
    )
  }
}
