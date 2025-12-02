package com.divinelink.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

actual class SystemUiController {
  actual fun setStatusBarColor(isLight: Boolean) {
    val statusBarStyle = if (isLight) {
      UIStatusBarStyleDarkContent
    } else {
      UIStatusBarStyleLightContent
    }
    UIApplication.sharedApplication.setStatusBarStyle(statusBarStyle, animated = true)
  }
}

@Composable
actual fun rememberSystemUiController(): SystemUiController = remember { SystemUiController() }
