package com.andreolas.movierama.base.data.remote.firebase.usecase

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.EncryptedStorage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

@Suppress("TooGenericExceptionThrown")
open class SetRemoteConfigUseCase(
  private val firebaseRemoteConfig: FirebaseRemoteConfig,
  private val encryptedPreferenceStorage: EncryptedStorage,
  dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.io) {
  override suspend fun execute(parameters: Unit) {
    val remoteTask = firebaseRemoteConfig.fetchAndActivate()
    if (remoteTask.exception != null) {
      throw Exception(remoteTask.exception?.message)
    }
    remoteTask.await()
    if (remoteTask.isSuccessful) {
      val apiKey = firebaseRemoteConfig.getString("tmdb_auth_token")
      encryptedPreferenceStorage.setTmdbAuthToken(apiKey)
      Result.success(Unit)
    } else {
      throw Exception("Couldn't fetch api key.")
    }
  }
}
