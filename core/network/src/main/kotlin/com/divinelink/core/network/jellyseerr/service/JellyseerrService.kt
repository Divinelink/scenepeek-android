package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow

interface JellyseerrService {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyseerrLoginData): Flow<JellyfinLoginResponseApi>

  suspend fun signInWithJellyseerr(jellyseerrLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun logout(address: String): Flow<Unit>

  suspend fun requestMedia(body: JellyseerrRequestMediaBodyApi): Flow<Unit>
}
