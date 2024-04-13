package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.base.storage.EncryptedStorage

open class FakeEncryptedPreferenceStorage(
    override var tmdbAuthToken: String = "",
) : EncryptedStorage {

    override suspend fun setTmdbAuthToken(key: String) {
        tmdbAuthToken = key
    }
}
