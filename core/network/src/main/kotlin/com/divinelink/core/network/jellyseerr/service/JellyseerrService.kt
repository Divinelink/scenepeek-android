package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import kotlinx.coroutines.flow.Flow

interface JellyseerrService {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyseerrLoginData): Flow<JellyfinLoginResponseApi>

  suspend fun signInWithJellyseerr(
    jellyseerrLogin: JellyseerrLoginData,
  ): Flow<JellyfinLoginResponseApi>

  suspend fun logout(address: String): Flow<Unit>

  // http://192.168.1.100:5055/api/v1/auth/me

  suspend fun requestMedia(body: JellyseerrRequestMediaBodyApi): Flow<JellyseerrResponseBodyApi>
}
