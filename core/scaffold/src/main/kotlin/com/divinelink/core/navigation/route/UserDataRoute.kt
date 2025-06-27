package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object UserDataRoute

fun NavController.navigateToUserData() = navigate(route = UserDataRoute)
