package com.andreolas.movierama

import com.google.common.truth.Truth.assertThat

abstract class ViewModelTestRobot<T> {

    abstract val actualViewState: T

    abstract fun buildViewModel(): ViewModelTestRobot<T>

    fun assertViewState(
        expectedViewState: T,
    ) = apply {
        assertThat(actualViewState).isEqualTo(expectedViewState)
    }

    fun assertFalseViewState(
        expectedViewState: T,
    ) = apply {
        assertThat(actualViewState).isNotEqualTo(expectedViewState)
    }
}
