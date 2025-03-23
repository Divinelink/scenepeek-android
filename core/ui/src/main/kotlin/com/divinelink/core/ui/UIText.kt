package com.divinelink.core.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.divinelink.core.model.UIText

/**
 * Evaluates the value of this [UIText] based on its type.
 *
 * @param[context] If necessary, use this to evaluate a string resource.
 */
@Suppress("SpreadOperator")
fun UIText.getString(context: Context): String = when (this) {
  is UIText.StringText -> this.value
  is UIText.ResourceText -> context.getString(this.value, *this.formatArgs)
}

/**
 * A helper function that allows to get strings from a [Composable] context.
 */
@Composable
fun UIText.getString(): String = this.getString(LocalContext.current)
