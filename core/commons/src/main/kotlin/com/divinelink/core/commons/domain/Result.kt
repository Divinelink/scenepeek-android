package com.divinelink.core.commons.domain

val <T> Result<T>.data: T
  get() = (this.getOrThrow())
