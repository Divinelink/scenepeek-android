package com.divinelink.core.testing

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class ViewModelTestRobot<T> {

  abstract val actualUiState: Flow<T>

  abstract fun buildViewModel(): ViewModelTestRobot<T>
}

fun <T, R : ViewModelTestRobot<T>> R.assertUiState(expectedUiState: T) = apply {
  assertThat((actualUiState as StateFlow).value).isEqualTo(expectedUiState)
}

fun <T, R : ViewModelTestRobot<T>> R.assertUiStateNotEqualTo(expectedUiState: T) = apply {
  assertThat(actualUiState).isNotEqualTo(expectedUiState)
}

/**
 * Launch a coroutine that will observe our [viewModel]'s view state and ensure that we consume
 * all of the supplied [uiStates] in the same order that they are supplied.
 *
 * We should call this near the front of the test, to ensure that every view state we emit
 * can be collected by this.
 */
suspend fun <T, R : ViewModelTestRobot<T>> R.expectUiStates(
  action: R.() -> Unit,
  uiStates: List<T>,
) = apply {
  actualUiState.test {
    action()

    for (state in uiStates) {
      assertThat(awaitItem()).isEqualTo(state)
    }

    this.cancel()
  }
}
