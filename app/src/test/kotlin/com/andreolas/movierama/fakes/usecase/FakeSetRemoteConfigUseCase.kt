package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.base.data.remote.firebase.usecase.SetRemoteConfigUseCase
import com.andreolas.movierama.fakes.remote.FakeRemoteConfig
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage

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
