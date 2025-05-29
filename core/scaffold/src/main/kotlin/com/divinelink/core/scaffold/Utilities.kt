package com.divinelink.core.scaffold

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
object UiTokens {

  val bottomNavHeight: Dp = 60.dp

  val navigationBarHeight: Dp
    @Composable get() = WindowInsets.navigationBars.asPaddingValues().run {
      calculateTopPadding() + calculateBottomPadding()
    }
}
