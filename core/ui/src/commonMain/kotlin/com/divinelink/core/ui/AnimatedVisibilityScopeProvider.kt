package com.divinelink.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.divinelink.core.ui.composition.PreviewLocalProvider

@Composable
fun SharedTransitionScopeProvider(
  content: @Composable AnimatedVisibilityScope.(
    sharedTransitionScope: SharedTransitionScope,
  ) -> Unit,
) {
  PreviewLocalProvider {
    SharedTransitionLayout {
      AnimatedVisibility(visible = true) {
        content(
          this@SharedTransitionLayout,
        )
      }
    }
  }
}
