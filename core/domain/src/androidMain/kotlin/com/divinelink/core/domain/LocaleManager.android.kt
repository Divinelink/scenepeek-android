package com.divinelink.core.domain

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class AndroidLocaleManager : LocaleManager {
  override fun currentLanguageTag(): String? =
    AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()

  override fun apply(tag: String?) {
    AppCompatDelegate.setApplicationLocales(
      LocaleListCompat.forLanguageTags(tag),
    )
  }
}

actual fun getLocaleManager(): LocaleManager = AndroidLocaleManager()
