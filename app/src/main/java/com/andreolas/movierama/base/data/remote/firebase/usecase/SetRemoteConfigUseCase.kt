package com.andreolas.movierama.base.data.remote.firebase.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.EncryptedStorage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("TooGenericExceptionThrown")
open class SetRemoteConfigUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val encryptedPreferenceStorage: EncryptedStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(parameters: Unit) {
        val remoteTask = firebaseRemoteConfig.fetchAndActivate()
        if (remoteTask.exception != null) {
            throw Exception(remoteTask.exception?.message)
        }
        remoteTask.await()
        if (remoteTask.isSuccessful) {
            val apiKey = firebaseRemoteConfig.getString("tmdb_api_key")
            encryptedPreferenceStorage.setTmdbApiKey(apiKey)
            Result.Success(Unit)
        } else {
            throw Exception("Couldn't fetch api key.")
        }
    }
}
