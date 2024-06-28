package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdJellyseerrService @Inject constructor(private val restClient: JellyseerrRestClient) :
  JellyseerrService {

  override suspend fun signInWithJellyfin(
    jellyfinLogin: JellyseerrLoginData,
  ): Flow<JellyfinLoginResponseApi> = flow {
    val url = "${jellyfinLogin.address}/api/v1/auth/jellyfin"

    val response = restClient.post<JellyfinLoginRequestBodyApi, JellyfinLoginResponseApi>(
      url = url,
      body = JellyfinLoginRequestBodyApi(
        username = jellyfinLogin.username.value,
        password = jellyfinLogin.password.value,
      ),
    )

    emit(response)
  }

  override suspend fun signInWithJellyseerr(jellyseerrLogin: JellyseerrLoginData): Flow<Unit> =
    flow {
      val url = "${jellyseerrLogin.address}/api/v1/auth/local"

      val response = restClient.post<JellyseerrLoginRequestBodyApi, Unit>(
        url = url,
        body = JellyseerrLoginRequestBodyApi(
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

  override suspend fun requestMedia(body: JellyseerrRequestMediaBodyApi): Flow<Unit> = flow {
    val url = "${body.address}/api/v1/request"

    restClient.post<JellyseerrRequestMediaBodyApi, Unit>(url = url, body = body)
    emit(Unit)
  }
}
