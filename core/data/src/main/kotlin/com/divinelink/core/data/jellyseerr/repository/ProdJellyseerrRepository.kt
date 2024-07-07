package com.divinelink.core.data.jellyseerr.repository

import com.divinelink.core.data.jellyseerr.mapper.map
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.map
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdJellyseerrRepository @Inject constructor(private val service: JellyseerrService) :
  JellyseerrRepository {

  override suspend fun signInWithJellyfin(
    loginData: JellyseerrLoginData,
  ): Flow<Result<JellyseerrAccountDetails>> = service
    .signInWithJellyfin(loginData)
    .map {
      Result.success(it.map())
    }
    .catch { error ->
      throw error
    }

  override suspend fun signInWithJellyseerr(
    loginData: JellyseerrLoginData,
  ): Flow<Result<JellyseerrAccountDetails>> = service
    .signInWithJellyseerr(loginData)
    .map {
      Result.success(it.map())
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

  override suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<Result<JellyseerrMediaRequest>> = service
    .requestMedia(body)
    .map {
      Result.success(it.map())
    }
    .catch { error ->
      throw error
    }
}
