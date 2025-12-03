package com.divinelink.core.ui.composition

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.divinelink.core.ui.manager.IntentManager

val LocalIntentManager: ProvidableCompositionLocal<IntentManager> = compositionLocalOf {
  error("LocalIntentManager not provided")
}
