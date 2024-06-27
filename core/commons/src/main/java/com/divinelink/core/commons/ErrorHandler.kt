package com.divinelink.core.commons

import com.divinelink.core.commons.ApiConstants.HTTP_ERROR_CODE

class ErrorHandler(private val throwable: Throwable) {

  private var actionMap = mutableMapOf<Int, (ErrorHandler) -> Unit>()
  private val exceptionActionMap: MutableMap<Class<out Exception>, (ErrorHandler) -> Unit> =
    mutableMapOf()

  companion object {
    fun create(throwable: Throwable): ErrorHandler = ErrorHandler(throwable)
  }

  fun on(
    vararg exception: Exception,
    action: (ErrorHandler) -> Unit,
  ) = apply {
    exception.forEach {
      exceptionActionMap[it::class.java] = action
    }
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
    // TODO This is a temporary fix to handle exceptions. We need to find a better way
    // to check instance of exception.
    val exceptionAction = exceptionActionMap.entries.firstOrNull {
      it.key.canonicalName == throwable.cause?.javaClass?.canonicalName
    }?.value

    action?.invoke(this)
    exceptionAction?.invoke(this)
  }

  fun otherwise(action: (ErrorHandler) -> Unit) = apply {
    action.invoke(this)
    return this
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
