package com.divinelink.feature.profile

import com.divinelink.core.model.user.data.UserDataSection

sealed interface ProfileUserInteraction {
  data object Login : ProfileUserInteraction
  data class NavigateToUserData(val section: UserDataSection) : ProfileUserInteraction
}
