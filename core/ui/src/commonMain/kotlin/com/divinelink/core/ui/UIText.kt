@file:Suppress("SpreadOperator")

package com.divinelink.core.ui

import androidx.compose.runtime.Composable
import com.divinelink.core.model.UIText
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * A helper function that allows to get strings from a [Composable] context.
 */
@Composable
fun UIText.getString(): String = when (this) {
  is UIText.StringText -> this.value
  is UIText.ResourceText -> stringResource(this.value, *this.formatArgs)
  is UIText.MultiResourceText -> {
    val args = this.formatArgs.map { stringResource(it) }.toTypedArray()

    return stringResource(this.value, *args)
  }
  is UIText.PluralResource -> pluralStringResource(this.value, this.size, *this.formatArgs)
}
