package com.divinelink.feature.settings.app.backup

data class BackupUiState(
  val loading: Boolean,
) {
  companion object {
    val initial = BackupUiState(
      loading = false,
    )
  }
}
