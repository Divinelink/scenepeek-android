package com.divinelink.core.designsystem.theme

import androidx.compose.runtime.Composable

expect class SystemUiController {
  fun setStatusBarColor(isLight: Boolean)
}

@Composable
expect fun rememberSystemUiController(): SystemUiController
