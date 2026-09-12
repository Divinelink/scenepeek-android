package com.divinelink.core.ui.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.divinelink.core.commons.provider.ClockProvider

val LocalClockProvider: ProvidableCompositionLocal<ClockProvider> = compositionLocalOf {
  error("LocalClockProvider not provided")
}
