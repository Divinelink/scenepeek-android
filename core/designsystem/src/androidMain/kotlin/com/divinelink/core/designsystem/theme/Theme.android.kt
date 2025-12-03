package com.divinelink.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
internal actual fun systemAppearance(
  dynamicColor: Boolean,
  blackBackground: Boolean,
  isDark: Boolean,
): ColorScheme {
  val colors = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val context = LocalContext.current
    if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
  } else {
    if (isDark) darkScheme else lightScheme
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
      WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
    }
  }

  if (!view.isInEditMode) {
    SideEffect {
      updateSystemBarsColor(view, !isDark)
    }
  }

  return colors
}
