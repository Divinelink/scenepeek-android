package com.andreolas.movierama.base.data.remote.domain.usecase

import com.andreolas.movierama.base.data.remote.firebase.usecase.SetRemoteConfigUseCase
import com.andreolas.movierama.fakes.remote.FakeRemoteConfig
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

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
    fakeRemoteConfig.mockGetApiKey("tmdb_auth_token", apiKey)

    useCase = SetRemoteConfigUseCase(
      firebaseRemoteConfig = fakeRemoteConfig.mock,
      encryptedPreferenceStorage = encryptedPreferenceStorage,
      dispatcher = testDispatcher,
    )

    val result = useCase.invoke(Unit)

    assertThat(result).isEqualTo(
      Result.success(Unit)
    )

    assertThat(
      encryptedPreferenceStorage.tmdbAuthToken
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
      Result.failure<Exception>(Exception("Couldn't fetch api key."))::class.java
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
      Result.failure<Exception>(Exception("General Εrror."))::class.java
    )
  }
}
