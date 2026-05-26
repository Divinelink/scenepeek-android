package com.divinelink.scenepeek.shared

import platform.Foundation.NSUserDefaults

actual fun getLocalManager(): LocaleManager = IosLocaleManager()

class IosLocaleManager : LocaleManager {
  override fun currentLanguageTag(): String? {
    return NSUserDefaults.standardUserDefaults.arrayForKey(LANGUAGE_KEY)?.firstOrNull()?.toString()
  }

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

