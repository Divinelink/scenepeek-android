package com.divinelink.core.ui

import androidx.compose.runtime.Composable
import com.divinelink.core.model.UIText
import org.jetbrains.compose.resources.stringResource

/**
 * A helper function that allows to get strings from a [Composable] context.
 */
@Composable
fun UIText.getString(): String = when (this) {
  is UIText.StringText -> this.value
  is UIText.ResourceText -> stringResource(this.value, *this.formatArgs)
}
