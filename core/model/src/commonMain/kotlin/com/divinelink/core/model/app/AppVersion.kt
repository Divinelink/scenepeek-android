package com.divinelink.core.model.app

data class AppVersion(
  val currentVersion: String,
  val latestVersion: String,
  val canSearchForUpdate: Boolean,
) {
  val hasNewUpdate
    get() = currentVersion < latestVersion
}
