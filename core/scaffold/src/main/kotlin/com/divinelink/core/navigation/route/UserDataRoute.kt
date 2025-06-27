package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.user.data.UserDataSection
import kotlinx.serialization.Serializable

@Serializable
data class UserDataRoute(val userDataSection: UserDataSection)

fun NavController.navigateToUserData(section: UserDataSection) = navigate(
  route = UserDataRoute(section),
)
