package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod

sealed interface JellyseerrInteraction {
  data class OnAddressChange(val address: String) : JellyseerrInteraction
  data class OnUsernameChange(val username: String) : JellyseerrInteraction
  data class OnPasswordChange(val password: String) : JellyseerrInteraction
  data class OnSelectLoginMethod(val signInMethod: JellyseerrLoginMethod) : JellyseerrInteraction
  data object OnLoginClick : JellyseerrInteraction
}
