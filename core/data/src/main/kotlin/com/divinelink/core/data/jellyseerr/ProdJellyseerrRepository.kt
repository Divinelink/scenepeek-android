package com.divinelink.core.data.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdJellyseerrRepository @Inject constructor(private val service: JellyseerrService) :
  JellyseerrRepository {

  override suspend fun signInWithJellyfin(loginData: JellyseerrLoginData): Flow<Result<String>> =
    service
      .signInWithJellyfin(loginData)
      .map {
        Result.success(it.jellyfinUsername)
      }
      .catch { error ->
        throw error
      }

  override suspend fun signInWithJellyseerr(loginData: JellyseerrLoginData): Flow<Result<String>> =
    service
      .signInWithJellyseerr(loginData)
      .map {
        Result.success(it.toString())
      }
      .catch { error ->
        throw error
      }

  override suspend fun logout(address: String): Flow<Result<Unit>> = service.logout(address)
    .map {
      Result.success(Unit)
    }
    .catch { error ->
      throw error
    }
}
