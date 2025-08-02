package com.divinelink.core.navigation.route

import androidx.navigation.NavController

fun NavController.navigateToOnboarding(fullscreen: Boolean) = navigate(
  route = if (fullscreen) {
    Navigation.Onboarding.FullScreenRoute
  } else {
    Navigation.Onboarding.ModalRoute
  },
)
