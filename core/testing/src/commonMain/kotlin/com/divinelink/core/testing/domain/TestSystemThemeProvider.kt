package com.divinelink.core.testing.domain

import com.divinelink.core.domain.theme.SystemThemeProvider

object SystemThemeProviderFactory {

  val available: SystemThemeProvider = object : SystemThemeProvider {
    override fun isSystemAvailable(): Boolean = true
  }

  val unavailable: SystemThemeProvider = object : SystemThemeProvider {
    override fun isSystemAvailable(): Boolean = true
  }
}
