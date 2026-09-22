package com.divinelink.core.model.preferences

import com.divinelink.core.commons.extensions.isBeforeNowMinus
import com.divinelink.core.commons.provider.ClockProvider
import kotlinx.datetime.LocalDateTime

data class BackupInfo(
  val lastBackupAt: LocalDateTime?,
  val automaticBackups: Boolean,
  val pathBackup: String?,
) {
  fun eligibleForAutoBackups(clock: ClockProvider) = automaticBackups &&
    pathBackup != null &&
    lastBackupAt.isBeforeNowMinus(
      clock = clock,
      days = 2,
    )

  companion object {
    val initial = BackupInfo(
      lastBackupAt = null,
      automaticBackups = false,
      pathBackup = null,
    )
  }
}
