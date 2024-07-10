package com.divinelink.core.commons

import com.divinelink.core.commons.exception.InvalidStatusException

class ErrorHandler(private val throwable: Throwable) {

  private var actions = mutableMapOf<Int, (ErrorHandler) -> Unit>()

  val exceptionActions: MutableMap<Class<out Throwable>, (ErrorHandler) -> Unit> = mutableMapOf()

  private var otherwiseAction: ((ErrorHandler) -> Unit)? = null

  companion object {
    fun create(throwable: Throwable): ErrorHandler = ErrorHandler(throwable)
  }

  fun on(
    errorCode: Int,
    action: (ErrorHandler) -> Unit,
  ) = apply {
    actions[errorCode] = action
    return this
  }

  inline fun <reified T : Exception> on(noinline action: (ErrorHandler) -> Unit): ErrorHandler =
    apply {
      exceptionActions[T::class.java] = action
    }

  /**
   * This method is used to handle the error in case no other error handler is found.
   * This is useful when you want to handle the error in a generic way.
   */
  fun otherwise(action: (ErrorHandler) -> Unit) = apply {
    otherwiseAction = action
    return this
  }

  /**
   * This method is used to handle the error based on the error code or the exception type.
   * If no error code is found, it will call the [otherwiseAction] method.
   */
  fun handle() {
    val errorCode = getErrorCode(throwable)
    val action = actions[errorCode]
    val exceptionAction = findExceptionAction(throwable)

    action?.invoke(this)
    exceptionAction?.invoke(this)

    if (action == null && exceptionAction == null) {
      otherwiseAction?.invoke(this)
    }
  }

  private fun findExceptionAction(throwable: Throwable?): ((ErrorHandler) -> Unit)? {
    if (throwable == null) return null

    val action = exceptionActions.entries.firstOrNull {
      it.key.isAssignableFrom(throwable.javaClass)
    }?.value

    return action ?: findExceptionAction(throwable.cause)
  }

  /**
   * Our [com.divinelink.core.network.client.androidClient] always throws [InvalidStatusException]
   * This method extracts the status code from the message.
   */
  private fun getErrorCode(throwable: Throwable): Int = when {
    throwable.cause is InvalidStatusException -> {
      val status = throwable.cause as InvalidStatusException
      status.status
    }
    throwable is InvalidStatusException -> {
      val status = throwable
      status.status
    }
    else -> -1
  }
}
