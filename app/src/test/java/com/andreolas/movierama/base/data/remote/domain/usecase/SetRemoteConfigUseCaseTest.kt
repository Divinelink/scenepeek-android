package com.andreolas.movierama.base.data.remote.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.firebase.usecase.SetRemoteConfigUseCase
import com.andreolas.movierama.fakes.remote.FakeRemoteConfig
import com.andreolas.movierama.test.util.fakes.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetRemoteConfigUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var useCase: SetRemoteConfigUseCase

    private val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    @Test
    fun `should return success when remote config is fetched successfully`() = runTest {
        val apiKey = "12345"
        val fakeRemoteConfig = FakeRemoteConfig()

        fakeRemoteConfig.mockFetchAndActivate(true)
        fakeRemoteConfig.mockGetApiKey("tmdb_api_key", apiKey)

        useCase = SetRemoteConfigUseCase(
            firebaseRemoteConfig = fakeRemoteConfig.mock,
            encryptedPreferenceStorage = encryptedPreferenceStorage,
            dispatcher = testDispatcher,
        )

        val result = useCase.invoke(Unit)

        assertThat(result).isEqualTo(
            Result.Success(Unit)
        )

        assertThat(
            encryptedPreferenceStorage.tmdbApiKey
        ).isEqualTo(
            apiKey
        )
    }

    @Test
    fun `should return error when remote config is not fetched successfully`() = runTest {
        val fakeRemoteConfig = FakeRemoteConfig()

        fakeRemoteConfig.mockFetchAndActivate(false)

        useCase = SetRemoteConfigUseCase(
            firebaseRemoteConfig = fakeRemoteConfig.mock,
            encryptedPreferenceStorage = encryptedPreferenceStorage,
            dispatcher = testDispatcher,
        )

        val result = useCase.invoke(Unit)

        assertThat(result).isInstanceOf(
            Result.Error(Exception("Couldn't fetch api key."))::class.java
        )
    }

    @Test
    fun `should return error when remote config has exception`() = runTest {
        val fakeRemoteConfig = FakeRemoteConfig()
        fakeRemoteConfig.mockException(Exception("General Error."))

        useCase = SetRemoteConfigUseCase(
            firebaseRemoteConfig = fakeRemoteConfig.mock,
            encryptedPreferenceStorage = encryptedPreferenceStorage,
            dispatcher = testDispatcher,
        )

        val result = useCase.invoke(Unit)

        assertThat(result).isInstanceOf(
            Result.Error(Exception("General Εrror."))::class.java
        )
    }
}
