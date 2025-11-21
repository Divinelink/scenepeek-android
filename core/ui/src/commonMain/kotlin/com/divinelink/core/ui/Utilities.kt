package com.divinelink.core.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.commons.provider.ConstantsProvider
import com.divinelink.core.commons.provider.getBuildConfigProvider
import com.divinelink.core.commons.provider.getConstantsProvider

typealias UiString = Res.string
typealias UiPlurals = Res.plurals
typealias UiDrawable = Res.drawable

@Stable
object UiTokens {

  val bottomNavHeight: Dp = 60.dp

  val navigationBarHeight: Dp
    @Composable get() = WindowInsets.navigationBars.asPaddingValues().run {
      calculateTopPadding() + calculateBottomPadding()
    }
}

@Composable
fun rememberConstants(): ConstantsProvider {
  return remember { getConstantsProvider() }
}

@Composable
fun rememberConfigProvider(): BuildConfigProvider {
  return remember { getBuildConfigProvider() }
}
