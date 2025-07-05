package com.divinelink.core.model

sealed class DisplayMessage(open val message: UIText) {
  data class Error(override val message: UIText) : DisplayMessage(message = message)
  data class Success(override val message: UIText) : DisplayMessage(message = message)
}
