package com.andreolas.movierama.base.data.remote.firebase.usecase

import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.commons.domain.UseCase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("TooGenericExceptionThrown")
open class SetRemoteConfigUseCase @Inject constructor(
  private val firebaseRemoteConfig: FirebaseRemoteConfig,
  private val encryptedPreferenceStorage: com.divinelink.core.datastore.EncryptedStorage,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(dispatcher) {
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
