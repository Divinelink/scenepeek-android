package com.divinelink.core.commons.domain

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
  val main: CoroutineDispatcher
  val default: CoroutineDispatcher
  val io: CoroutineDispatcher
  val unconfined: CoroutineDispatcher
}
