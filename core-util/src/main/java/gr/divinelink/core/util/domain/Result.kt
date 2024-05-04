package gr.divinelink.core.util.domain

val <T> Result<T>.data: T
  get() = (this.getOrThrow())
