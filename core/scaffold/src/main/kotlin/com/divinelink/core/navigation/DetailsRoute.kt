package com.divinelink.core.navigation

import androidx.navigation.NavController
import com.divinelink.core.navigation.route.DetailsRoute
import kotlinx.serialization.Serializable

@Serializable
object DetailsRoute

fun NavController.navigateToDetails(route: DetailsRoute) = navigate(route = route)
