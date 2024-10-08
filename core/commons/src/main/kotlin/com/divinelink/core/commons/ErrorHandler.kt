package com.divinelink.core.commons

import com.divinelink.core.commons.exception.InvalidStatusException

class ErrorHandler private constructor() {

  private var actions = mutableMapOf<Int, (ErrorHandler) -> Unit>()
  private var otherwiseAction: ((Throwable) -> Unit)? = null
  val exceptionActions: MutableMap<Class<out Throwable>, (Throwable) -> Unit> = mutableMapOf()

  companion object {
    val global = ErrorHandler()

    fun create(
      throwable: Throwable,
      actions: ErrorHandler.() -> Unit,
    ) = ErrorHandler().apply(actions).handle(throwable)
  }

  /**
   * Registers an action to be executed when an error with the specified code occurs.
   */
  fun on(
    errorCode: Int,
    action: (ErrorHandler) -> Unit,
  ) = apply {
    actions[errorCode] = action
  }

  /**
   * Registers an action to be executed when an exception of the specified type occurs.
   */
  inline fun <reified T : Throwable> on(noinline action: (Throwable) -> Unit): ErrorHandler =
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
  fun handle(
    throwable: Throwable,
    skipGlobal: Boolean = false,
  ) {
    val errorCode = getErrorCode(throwable)
    val action = actions[errorCode]
    val exceptionAction = findExceptionAction(throwable)

    action?.invoke(this)
    exceptionAction?.invoke(throwable)

    if (action == null && exceptionAction == null) {
      otherwiseAction?.invoke(throwable)
    }

    // If global handling is not skipped, pass the error to the global handler
    if (!skipGlobal) {
      // Ensure global handler doesn't recurse
      global.handle(throwable, skipGlobal = true)
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
    throwable is InvalidStatusException -> throwable.status
    else -> -1
  }
}
