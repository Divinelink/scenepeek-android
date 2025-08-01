package com.divinelink.feature.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.feature.details.person.ui.PersonScreen

fun NavGraphBuilder.personScreen(onNavigate: (Navigation) -> Unit) {
  composable<PersonRoute> {
    PersonScreen(
      onNavigate = onNavigate,
      animatedVisibilityScope = this@composable,
    )
  }
}
