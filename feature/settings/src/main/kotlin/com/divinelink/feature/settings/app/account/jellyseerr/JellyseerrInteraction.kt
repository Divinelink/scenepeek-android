package com.divinelink.feature.settings.app.account.jellyseerr

sealed interface JellyseerrInteraction {
  data class OnApiKeyChange(val key: String) : JellyseerrInteraction
  data class OnAddressChange(val address: String) : JellyseerrInteraction
  data object OnTestClick : JellyseerrInteraction
  data object OnSaveClick : JellyseerrInteraction
}
