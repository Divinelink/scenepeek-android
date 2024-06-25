package com.divinelink.core.data.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyfinLogin
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdJellyseerrRepository @Inject constructor(
  private val jellyseerrService: JellyseerrService,
) : JellyseerrRepository {

  override suspend fun signInWithJellyfin(jellyfinLogin: JellyfinLogin): Flow<Result<String>> =
    jellyseerrService
      .signInWithJellyfin(jellyfinLogin)
      .map {
        Result.success(it.jellyfinUsername)
      }
      .catch { error ->
        throw error
      }
}
