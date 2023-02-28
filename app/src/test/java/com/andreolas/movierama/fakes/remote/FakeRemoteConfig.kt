package com.andreolas.movierama.fakes.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.coEvery
import io.mockk.mockk

class FakeRemoteConfig {

    val mock: FirebaseRemoteConfig = mockk()

    fun mockFetchAndActivate(
        response: Boolean,
    ) {
        coEvery {
            mock.fetchAndActivate().isSuccessful
        } returns response

        coEvery {
            mock.fetchAndActivate().isComplete
        } returns true

        coEvery {
            mock.fetchAndActivate().result
        } returns true

        coEvery {
            mock.fetchAndActivate().isCanceled
        } returns false

        coEvery {
            mock.fetchAndActivate().exception
        } returns null
    }

    fun mockException(
        exception: Exception,
    ) {
        coEvery {
            mock.fetchAndActivate().isSuccessful
        } returns true

        coEvery {
            mock.fetchAndActivate().isComplete
        } returns true

        coEvery {
            mock.fetchAndActivate().result
        } returns false

        coEvery {
            mock.fetchAndActivate().isCanceled
        } returns true

        coEvery {
            mock.fetchAndActivate().exception
        } returns exception
    }

    fun mockGetApiKey(
        key: String,
        response: String,
    ) {
        coEvery {
            mock.getString(key)
        } returns response
    }
}
