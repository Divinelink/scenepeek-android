package com.divinelink.scenepeek.shared

interface LocaleManager {
  fun currentLanguageTag(): String?
  fun apply(tag: String?)
}

expect fun getLocalManager(): LocaleManager
