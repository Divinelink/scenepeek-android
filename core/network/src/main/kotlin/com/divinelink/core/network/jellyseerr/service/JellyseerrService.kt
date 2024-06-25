package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyfinLogin
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import kotlinx.coroutines.flow.Flow

interface JellyseerrService {

  suspend fun signInWithJellyfin(jellyfinLogin: JellyfinLogin): Flow<JellyfinLoginResponseApi>
}
