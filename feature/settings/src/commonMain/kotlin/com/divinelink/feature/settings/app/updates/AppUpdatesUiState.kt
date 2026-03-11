package com.divinelink.feature.settings.app.updates

import com.divinelink.core.model.app.AppVersion

data class AppUpdatesUiState(
  val appVersion: AppVersion?,
  val fallbackVersion: String,
) {
  companion object {
    fun initial(version: String) = AppUpdatesUiState(
      appVersion = null,
      fallbackVersion = version,
    )
  }
}
