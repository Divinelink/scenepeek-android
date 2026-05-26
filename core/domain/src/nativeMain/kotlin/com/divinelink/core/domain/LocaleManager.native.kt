package com.divinelink.core.domain

import platform.Foundation.NSUserDefaults

class IosLocaleManager : LocaleManager {
  override fun currentLanguageTag(): String? =
    NSUserDefaults.standardUserDefaults.arrayForKey(LANGUAGE_KEY)?.firstOrNull()?.toString()

  override fun apply(tag: String?) {
    val defaults = NSUserDefaults.standardUserDefaults
    if (tag == null) {
      defaults.removeObjectForKey(LANGUAGE_KEY)
    } else {
      defaults.setObject(listOf(tag), forKey = LANGUAGE_KEY)
    }
    defaults.synchronize()
  }

  private companion object {
    const val LANGUAGE_KEY = "AppleLanguages"
  }
}

actual fun getLocaleManager(): LocaleManager = IosLocaleManager()
