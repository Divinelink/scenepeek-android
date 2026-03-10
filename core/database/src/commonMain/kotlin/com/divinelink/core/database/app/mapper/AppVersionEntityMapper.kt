package com.divinelink.core.database.app.mapper

import com.divinelink.core.database.AppVersionEntity
import com.divinelink.core.database.isOlderThanCurrentTime
import com.divinelink.core.model.app.AppVersion
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

fun AppVersionEntity.map(
  clock: Clock,
  defaultVersion: String,
) = AppVersion(
  currentVersion = currentVersion ?: defaultVersion,
  latestVersion = latestVersion ?: defaultVersion,
  canSearchForUpdate = lastCheck?.isOlderThanCurrentTime(
    clock = clock,
    duration = 4.hours,
  ) == true,
)
