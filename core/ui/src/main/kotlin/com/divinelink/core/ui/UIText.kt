package com.divinelink.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * This is a sealed class that contains all of the different ways text can be presented to the UI.
 */
sealed class UIText {
  data class StringText(val value: String) : UIText()

  class ResourceText(
    @StringRes val value: Int,
    vararg val formatArgs: Any
  ) : UIText() {

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as ResourceText

      if (value != other.value) return false
      if (!formatArgs.contentEquals(other.formatArgs)) return false

      return true
    }

    override fun hashCode(): Int {
      var result = value.hashCode()
      result = 31 * result + formatArgs.contentHashCode()
      return result
    }
  }
}

/**
 * Evaluates the value of this [UIText] based on its type.
 *
 * @param[context] If necessary, use this to evaluate a string resource.
 */
@Suppress("SpreadOperator")
fun UIText.getString(context: Context): String {
  return when (this) {
    is UIText.StringText -> this.value
    is UIText.ResourceText -> context.getString(this.value, *this.formatArgs)
  }
}

/**
 * A helper function that allows to get strings from a [Composable] context.
 */
@Composable
fun UIText.getString(): String {
  return this.getString(LocalContext.current)
}
