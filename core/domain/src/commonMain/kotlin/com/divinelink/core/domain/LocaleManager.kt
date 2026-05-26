package com.divinelink.core.domain

interface LocaleManager {
  fun currentLanguageTag(): String?
  fun apply(tag: String?)
}

expect fun getLocaleManager(): LocaleManager
