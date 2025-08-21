@file:Suppress("PropertyName", "ConstructorParameterNaming")

package com.divinelink.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
  val keyline_0: Dp = 0.dp,
  val keyline_1: Dp = 1.dp,
  val keyline_2: Dp = 2.dp,
  val keyline_4: Dp = 4.dp,
  val keyline_6: Dp = 6.dp,
  val keyline_8: Dp = 8.dp,
  val keyline_10: Dp = 10.dp,
  val keyline_12: Dp = 12.dp,
  val keyline_13: Dp = 13.dp,
  val keyline_14: Dp = 14.dp,
  val keyline_16: Dp = 16.dp,
  val keyline_17: Dp = 17.dp,
  val keyline_20: Dp = 20.dp,
  val keyline_24: Dp = 24.dp,
  val keyline_26: Dp = 26.dp,
  val keyline_28: Dp = 28.dp,
  val keyline_32: Dp = 32.dp,
  val keyline_36: Dp = 36.dp,
  val keyline_40: Dp = 40.dp,
  val keyline_48: Dp = 48.dp,
  val keyline_56: Dp = 56.dp,
  val keyline_58: Dp = 58.dp,
  val keyline_64: Dp = 64.dp,
  val keyline_68: Dp = 68.dp,
  val keyline_72: Dp = 72.dp,
  val keyline_84: Dp = 84.dp,
  val keyline_96: Dp = 96.dp,
  val shortMediaCard: Dp = 120.dp,
  val posterSizeSmall: Dp = 140.dp,
  val posterSize: Dp = 180.dp,
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun mediaCardSize() = if (currentWindowDpSize().width < 400.dp) {
  MaterialTheme.dimensions.keyline_96
} else {
  MaterialTheme.dimensions.shortMediaCard
}

val MaterialTheme.dimensions: Dimensions
  @Composable
  @ReadOnlyComposable
  get() = LocalDimensions.current

internal val LocalDimensions = staticCompositionLocalOf { Dimensions() }
