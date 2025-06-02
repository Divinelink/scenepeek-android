package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

fun NavController.navigateToOnboarding() = navigate(route = OnboardingRoute)
