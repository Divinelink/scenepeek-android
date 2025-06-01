package com.divinelink.core.navigation

import androidx.navigation.NavController
import com.divinelink.core.navigation.route.PersonRoute

fun NavController.navigateToPerson(route: PersonRoute) = navigate(route = route)
