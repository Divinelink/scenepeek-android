package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import kotlinx.coroutines.flow.Flow

interface JellyseerrService {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun signInWithJellyseerr(jellyseerrLogin: JellyseerrLoginData): Flow<Unit>

  suspend fun fetchAccountDetails(address: String): Flow<JellyseerrAccountDetailsResponseApi>

  suspend fun logout(address: String): Flow<Unit>

  suspend fun requestMedia(body: JellyseerrRequestMediaBodyApi): Flow<JellyseerrResponseBodyApi>
}
