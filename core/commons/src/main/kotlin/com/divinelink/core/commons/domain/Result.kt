package com.divinelink.core.commons.domain

val <T> Result<T>.data: T
  get() = (this.getOrThrow())

inline fun <reified E : Throwable> Result<*>.onError(crossinline action: (E) -> Unit): Result<*> =
  onFailure {
    if (it is E) {
      action(it)
    }
  }
