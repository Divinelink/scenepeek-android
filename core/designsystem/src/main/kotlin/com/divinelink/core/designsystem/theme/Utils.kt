package com.divinelink.core.designsystem.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

fun updateStatusBarColor(
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
