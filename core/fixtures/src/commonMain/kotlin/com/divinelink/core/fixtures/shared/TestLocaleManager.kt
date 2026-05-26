package com.divinelink.core.fixtures.shared

import com.divinelink.core.domain.LocaleManager

class TestLocaleManager : LocaleManager {

  override fun apply(tag: String?) {
    // Do nothing
  }

  override fun currentLanguageTag(): String? = null
}
