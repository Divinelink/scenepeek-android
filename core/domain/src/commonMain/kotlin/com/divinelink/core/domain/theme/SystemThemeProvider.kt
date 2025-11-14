package com.divinelink.core.domain.theme

interface SystemThemeProvider {
  fun isSystemAvailable(): Boolean
}

class ProdSystemThemeProvider : SystemThemeProvider {
  override fun isSystemAvailable(): Boolean = isSystemThemeAvailable()
}

expect fun isSystemThemeAvailable(): Boolean
