package com.divinelink.feature.settings.app.backup

import com.divinelink.core.model.track.backup.RestoreProcess
import com.divinelink.core.ui.components.dialog.DialogState

data class BackupUiState(
  val loading: Boolean,
  val restoreProcess: RestoreProcess?,
  val dialog: DialogState?,
) {
  companion object {
    val initial = BackupUiState(
      loading = false,
      restoreProcess = null,
      dialog = null,
    )
  }
}
