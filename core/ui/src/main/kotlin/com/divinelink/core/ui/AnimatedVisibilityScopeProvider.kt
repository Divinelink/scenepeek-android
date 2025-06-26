package com.divinelink.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.AppTheme

@Composable
fun AnimatedVisibilityScopeProvider(
  content: @Composable (
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
  ) -> Unit,
) {
  AppTheme {
    Surface {
      SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
          content(
            this@SharedTransitionLayout,
            this,
          )
        }
      }
    }
  }
}

@Composable
fun SharedTransitionScopeProvider(
  content: @Composable AnimatedVisibilityScope.(
    sharedTransitionScope: SharedTransitionScope,
  ) -> Unit,
) {
  AppTheme {
    Surface {
      SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
          content(
            this@SharedTransitionLayout,
          )
        }
      }
    }
  }
}
