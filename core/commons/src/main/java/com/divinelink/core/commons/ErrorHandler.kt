package com.divinelink.core.commons

import com.divinelink.core.commons.ApiConstants.HTTP_ERROR_CODE

class ErrorHandler(private val throwable: Throwable) {

  private var actionMap = mutableMapOf<Int, (ErrorHandler) -> Unit>()
  private var exceptionActionMap = mutableMapOf<Exception, (ErrorHandler) -> Unit>()

  companion object {
    fun create(throwable: Throwable): ErrorHandler = ErrorHandler(throwable)
  }

  fun on(
    exception: Exception,
    action: (ErrorHandler) -> Unit,
  ) = apply {
    exceptionActionMap[exception] = action
    return this
  }

  fun on(
    errorCode: Int,
    action: (ErrorHandler) -> Unit,
  ) = apply {
    actionMap[errorCode] = action
    return this
  }

  fun handle() {
    val errorCode = getErrorCode(throwable)
    val action = actionMap[errorCode]
    val exceptionAction = exceptionActionMap[throwable]
    action?.invoke(this)
    exceptionAction?.invoke(this)
  }

  fun otherwise(action: (ErrorHandler) -> Unit) = apply {
    actionMap[0] = action
  }

  /**
   * Our [com.divinelink.core.network.client.androidClient] always throws an [Exception]
   * with the status code in the message. This method extracts the status code from the message.
   */
  private fun getErrorCode(throwable: Throwable): Int {
    val message = throwable.message ?: ""
    val statusCode = message.substringAfter(HTTP_ERROR_CODE).replace("\"", "")
    return statusCode.toIntOrNull() ?: 0
  }
}
