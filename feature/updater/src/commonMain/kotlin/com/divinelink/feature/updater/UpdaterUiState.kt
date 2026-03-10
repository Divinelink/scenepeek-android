package com.divinelink.feature.updater

import com.divinelink.core.model.LCEState
import com.divinelink.core.model.app.AppVersion

data class UpdaterUiState(
  val appVersion: LCEState<AppVersion>,
) {
  companion object {
    val initial = UpdaterUiState(
      appVersion = LCEState.Loading,
    )
  }
}
