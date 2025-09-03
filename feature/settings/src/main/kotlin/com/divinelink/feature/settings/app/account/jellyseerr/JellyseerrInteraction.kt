package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod

sealed interface JellyseerrInteraction {
  data class OnAddressChange(val address: String) : JellyseerrInteraction
  data class OnUsernameChange(val username: String) : JellyseerrInteraction
  data class OnPasswordChange(val password: String) : JellyseerrInteraction
  data class OnSelectLoginMethod(val signInMethod: JellyseerrAuthMethod) : JellyseerrInteraction
  data object OnLoginClick : JellyseerrInteraction
  data object OnLogoutClick : JellyseerrInteraction
  data object OnDismissDialog : JellyseerrInteraction
}
