package com.divinelink.core.commons

import com.divinelink.core.commons.exception.InvalidStatusException

class ErrorHandler(private val throwable: Throwable) {

  private var actions = mutableMapOf<Int, (ErrorHandler) -> Unit>()

  val exceptionActions: MutableMap<Class<out Throwable>, (Throwable) -> Unit> = mutableMapOf()

  private var otherwiseAction: ((Throwable) -> Unit)? = null

  companion object {
    fun create(
      throwable: Throwable,
      actions: ErrorHandler.() -> Unit,
    ) = ErrorHandler(throwable).apply(actions).handle()
  }

  fun on(
    errorCode: Int,
    action: (ErrorHandler) -> Unit,
  ) = apply {
    actions[errorCode] = action
    return this
  }

  inline fun <reified T : Exception> on(noinline action: (Throwable) -> Unit): ErrorHandler =
    apply {
      exceptionActions[T::class.java] = action
    }

  /**
   * This method is used to handle the error in case no other error handler is found.
   * This is useful when you want to handle the error in a generic way.
   */
  fun otherwise(action: (Throwable) -> Unit) = apply {
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
    exceptionAction?.invoke(throwable)

    if (action == null && exceptionAction == null) {
      otherwiseAction?.invoke(throwable)
    }
  }

  private fun findExceptionAction(throwable: Throwable?): ((Throwable) -> Unit)? {
    if (throwable == null) return null

    val action = exceptionActions.entries.firstOrNull {
      it.key.isAssignableFrom(throwable.javaClass)
    }?.value

    return action ?: findExceptionAction(throwable.cause)
  }

  /**
   * Our [com.divinelink.core.network.client.ktorClient] always throws [InvalidStatusException]
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
