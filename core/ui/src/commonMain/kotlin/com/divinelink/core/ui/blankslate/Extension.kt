package com.divinelink.core.ui.blankslate

import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException

fun Throwable.toBlankSlateState(
  retryText: UIText? = null,
) = when (this) {
  is AppException.Offline -> BlankSlateState.Offline
  is AppException.Unauthorized -> BlankSlateState.Unauthenticated(
    retryText = retryText,
  )
  else -> BlankSlateState.Generic
}
