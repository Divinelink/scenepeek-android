package com.divinelink.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.AppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
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
