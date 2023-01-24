package com.andreolas.movierama.base.data.remote.firebase.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.EncryptedPreferenceStorage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

open class SetRemoteConfigUseCase @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val encryptedPreferenceStorage: EncryptedPreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(parameters: Unit) {
        val remoteTask = firebaseRemoteConfig.fetchAndActivate()
        remoteTask.await()
        if (remoteTask.isSuccessful) {
            val apiKey = firebaseRemoteConfig.getString("tmdb_api_key")
            encryptedPreferenceStorage.setTmdbApiKey(apiKey)
            Result.Success(Unit)
        } else {
            Result.Error(Exception("Couldn't fetch api key."))
        }
    }
}
