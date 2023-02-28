package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.firebase.usecase.SetRemoteConfigUseCase
import com.andreolas.movierama.fakes.remote.FakeRemoteConfig
import com.andreolas.movierama.test.util.fakes.FakeEncryptedPreferenceStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class FakeSetRemoteConfigUseCase : SetRemoteConfigUseCase(
    firebaseRemoteConfig = FakeRemoteConfig().mock,
    encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(),
    dispatcher = MainDispatcherRule().testDispatcher,
) {
    private var resultSetRemoteConfig: MutableMap<Unit, Unit> = mutableMapOf()

    fun mockSetRemoteConfigResult(
        result: Unit,
    ) {
        resultSetRemoteConfig[Unit] = result
    }

    override suspend fun execute(parameters: Unit) {
        return resultSetRemoteConfig[parameters]!!
    }
}
