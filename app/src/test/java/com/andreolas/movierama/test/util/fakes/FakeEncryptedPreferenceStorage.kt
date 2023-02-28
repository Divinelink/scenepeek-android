package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.base.storage.EncryptedStorage

open class FakeEncryptedPreferenceStorage(
    override var tmdbApiKey: String = "",
) : EncryptedStorage {

    override suspend fun setTmdbApiKey(key: String) {
        tmdbApiKey = key
    }
}
