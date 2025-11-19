package com.divinelink.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.NavType
import com.divinelink.feature.search.ui.SearchScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.searchScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.SearchRoute>(
    typeMap = mapOf(typeOf<SearchEntryPoint>() to NavType.SearchEntryPoint),
  ) {
    SearchScreen(onNavigate = onNavigate)
  }
}
