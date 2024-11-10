package com.divinelink.scenepeek.fakes.usecase

import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.scenepeek.base.data.remote.firebase.usecase.SetRemoteConfigUseCase
import com.divinelink.scenepeek.fakes.remote.FakeRemoteConfig

class FakeSetRemoteConfigUseCase :
  SetRemoteConfigUseCase(
    firebaseRemoteConfig = FakeRemoteConfig().mock,
    encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(),
    dispatcher = MainDispatcherRule().testDispatcher,
  ) {
  private var resultSetRemoteConfig: MutableMap<Unit, Unit> = mutableMapOf()

  fun mockSetRemoteConfigResult(result: Unit) {
    resultSetRemoteConfig[Unit] = result
  }

  override suspend fun execute(parameters: Unit) = resultSetRemoteConfig[parameters]!!
}
