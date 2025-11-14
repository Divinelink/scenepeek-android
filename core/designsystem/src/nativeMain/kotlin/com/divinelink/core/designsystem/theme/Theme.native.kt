package com.divinelink.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
internal actual fun systemAppearance(
  dynamicColor: Boolean,
  blackBackground: Boolean,
  isDark: Boolean,
): ColorScheme {
  LaunchedEffect(isDark) {
    UIApplication.sharedApplication.setStatusBarStyle(
      if (isDark) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent,
    )
  }

  return if (isDark) darkScheme else lightScheme
}
