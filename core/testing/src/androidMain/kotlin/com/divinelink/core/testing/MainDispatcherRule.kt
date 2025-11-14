package com.divinelink.core.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainDispatcherRule(val testDispatcher: TestDispatcherProvider = TestDispatcherProvider()) :
  TestWatcher() {
  override fun starting(description: Description) {
    super.starting(description)
    Dispatchers.setMain(testDispatcher.default)
  }

  override fun finished(description: Description) {
    super.finished(description)
    Dispatchers.resetMain()
  }
}
