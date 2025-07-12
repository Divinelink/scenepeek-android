package com.divinelink.feature.profile

import com.divinelink.core.model.user.data.UserDataSection

sealed interface ProfileAction {
  data object Login : ProfileAction
  data class NavigateToUserData(val section: UserDataSection) : ProfileAction
  data object NavigateToLists : ProfileAction
}
