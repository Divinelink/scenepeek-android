package com.divinelink.core.commons

val <T> Result<T>.data: T
  get() = (this.getOrThrow())

// TODO Add tests to check that the Result is
//  converted to success after onError and that chain is broken
inline fun <reified E : Throwable> Result<*>.onError(crossinline action: (E) -> Unit): Result<*> =
  (exceptionOrNull() as? E)?.let { exception ->
    action(exception)
    Result.success(Unit) // Convert to success to break the chain
  } ?: this
