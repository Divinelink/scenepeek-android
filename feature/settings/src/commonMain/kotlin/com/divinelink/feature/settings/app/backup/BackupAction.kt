package com.divinelink.feature.settings.app.backup

sealed interface BackupAction {
  data class UpdateAutomaticBackups(val enabled: Boolean) : BackupAction
  data object SetBackupLocation : BackupAction
}
