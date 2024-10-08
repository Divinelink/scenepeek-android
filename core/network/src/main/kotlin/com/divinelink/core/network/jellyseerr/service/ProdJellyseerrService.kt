package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdJellyseerrService(private val restClient: JellyseerrRestClient) : JellyseerrService {

  override suspend fun signInWithJellyfin(
    jellyfinLogin: JellyseerrLoginData,
  ): Flow<JellyfinLoginResponseApi> = flow {
    val url = "${jellyfinLogin.address}/api/v1/auth/jellyfin"

    val response = restClient.post<JellyseerrLoginRequestBodyApi, JellyfinLoginResponseApi>(
      url = url,
      body = JellyseerrLoginRequestBodyApi.Jellyfin(
        username = jellyfinLogin.username.value,
        password = jellyfinLogin.password.value,
      ),
    )

    emit(response)
  }

  override suspend fun signInWithJellyseerr(
    jellyseerrLogin: JellyseerrLoginData,
  ): Flow<JellyfinLoginResponseApi> = flow {
    val url = "${jellyseerrLogin.address}/api/v1/auth/local"

    val response = restClient.post<JellyseerrLoginRequestBodyApi, JellyfinLoginResponseApi>(
      url = url,
      body = JellyseerrLoginRequestBodyApi.Jellyseerr(
        email = jellyseerrLogin.username.value,
        password = jellyseerrLogin.password.value,
      ),
    )

    emit(response)
  }

  override suspend fun logout(address: String): Flow<Unit> = flow {
    val url = "$address/api/v1/auth/logout"

    restClient.post<Unit, Unit>(url = url, body = Unit)

    emit(Unit)
  }

  override suspend fun requestMedia(
    body: JellyseerrRequestMediaBodyApi,
  ): Flow<JellyseerrResponseBodyApi> = flow {
    val url = "${body.address}/api/v1/request"

    val response = restClient.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
      url = url,
      body = body,
    )

    emit(response)
  }
}
