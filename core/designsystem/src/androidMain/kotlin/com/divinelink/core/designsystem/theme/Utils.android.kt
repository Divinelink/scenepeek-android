package com.divinelink.core.designsystem.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

fun updateSystemBarsColor(
  view: View,
  setLight: Boolean,
): Window {
  val window = (view.context as Activity).window
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    window.isNavigationBarContrastEnforced = false
  } else {
    window.navigationBarColor = Color.Transparent.toArgb()
  }

  WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = setLight
  WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = setLight

  return window
}

@Composable
actual fun rememberSystemUiController(): SystemUiController {
  val view = LocalView.current
  return remember(view) { SystemUiController(view) }
}

actual class SystemUiController(val view: View) {
  actual fun setStatusBarColor(isLight: Boolean) {
    val window = (view.context as Activity).window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      window.isNavigationBarContrastEnforced = false
    } else {
      window.navigationBarColor = Color.Transparent.toArgb()
    }

    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLight
  }
}
