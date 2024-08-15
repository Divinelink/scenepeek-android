package com.divinelink.core.testing

import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private val testScheduler = UnconfinedTestDispatcher(TestCoroutineScheduler())

class TestDispatcherProvider : DispatcherProvider {
  override val main: CoroutineDispatcher = testScheduler
  override val default: CoroutineDispatcher = testScheduler
  override val io: CoroutineDispatcher = testScheduler
  override val unconfined: CoroutineDispatcher = testScheduler
}
