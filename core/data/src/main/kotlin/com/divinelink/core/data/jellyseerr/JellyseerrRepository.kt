package com.divinelink.core.data.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyfinLogin
import kotlinx.coroutines.flow.Flow

interface JellyseerrRepository {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyfinLogin): Flow<Result<String>>

  suspend fun logout(address: String): Flow<Result<Unit>>
}
