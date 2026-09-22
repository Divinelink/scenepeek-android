package com.divinelink.core.model.preferences

import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language

data class AppSettings(
  val enablePeriodicBackups: Boolean,
  val backupPath: String?,
  val metadataLanguage: Language,
  val inAppUpdaterOptIn: Boolean,
  val visibleStreamingServices: Boolean,
  val region: Country,
) {
  companion object {
    val initial = AppSettings(
      enablePeriodicBackups = true,
      backupPath = null,
      metadataLanguage = Language.ENGLISH,
      inAppUpdaterOptIn = true,
      visibleStreamingServices = true,
      region = Country.UNITED_STATES,
    )
  }
}
