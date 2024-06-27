package com.divinelink.core.data.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import kotlinx.coroutines.flow.Flow

interface JellyseerrRepository {

  suspend fun signInWithJellyfin(loginData: JellyseerrLoginData): Flow<Result<String>>

  suspend fun signInWithJellyseerr(loginData: JellyseerrLoginData): Flow<Result<String>>

  suspend fun logout(address: String): Flow<Result<Unit>>
}
