package com.divinelink.core.model

/**
 * This is a sealed class that contains all of the different ways text can be presented to the UI.
 */
sealed class UIText {
  data class StringText(val value: String) : UIText()

  class ResourceText(
    val value: Int,
    vararg val formatArgs: Any,
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
