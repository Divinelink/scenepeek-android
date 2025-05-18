package com.divinelink.core.ui.fullscreen

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun FullscreenMode(content: @Composable () -> Unit) {
  val view = LocalView.current
  val window = (LocalView.current.context as Activity).window
  // Hide system bars when entering the composable
  SideEffect {
    WindowInsetsControllerCompat(window, view).let { controller ->
      controller.hide(WindowInsetsCompat.Type.statusBars())
      controller.hide(WindowInsetsCompat.Type.navigationBars())
      // Optionally improve immersive experience
      controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
  }

  // Show system bars when leaving the composable
  DisposableEffect(Unit) {
    onDispose {
      WindowInsetsControllerCompat(window, view).let { controller ->
        controller.show(WindowInsetsCompat.Type.statusBars())
        controller.show(WindowInsetsCompat.Type.navigationBars())
      }
    }
  }

  content()
}
