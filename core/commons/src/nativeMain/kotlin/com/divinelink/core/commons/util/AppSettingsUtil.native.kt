package com.divinelink.core.commons.util

import androidx.compose.runtime.Composable

actual class AppUtilController {
  actual fun navigateToAppSettings() {
    // TODO
  }
}

@Composable
actual fun rememberAppController(): AppUtilController {
  return AppUtilController()
}
