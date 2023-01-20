package com.andreolas.movierama.settings.appearance.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.settings.app.appearance.usecase.SetThemeUseCase
import com.andreolas.movierama.test.util.fakes.FakePreferenceStorage
import com.andreolas.movierama.ui.theme.Theme
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SetThemeUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var fakePreferenceStorage: FakePreferenceStorage

    @Before
    fun setUp() {
        fakePreferenceStorage = FakePreferenceStorage()
    }

    @Test
    fun `correctly set theme use case`() = runTest {
        // Given
        val response = Result.Success(Unit)
        val failResponse = Result.Error(Exception("Some exception"))
        // When
        val useCase = SetThemeUseCase(fakePreferenceStorage, testDispatcher)
        val result = useCase(Theme.LIGHT)
        // Then
        assertEquals(response, result)
        assertNotEquals(failResponse, result)
    }
}
