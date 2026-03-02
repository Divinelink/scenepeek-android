package com.divinelink.core.ui.composition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.divinelink.core.model.preferences.DetailPreferences

val LocalDetailPreferences = compositionLocalOf<DetailPreferences> {
  error("DetailPreferences not provided")
}

@Composable
fun rememberDetailPreferences(): DetailPreferences = LocalDetailPreferences.current
