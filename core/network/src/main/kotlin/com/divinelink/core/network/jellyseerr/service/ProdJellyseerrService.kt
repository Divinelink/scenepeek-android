package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.model.jellyseerr.JellyfinLogin
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdJellyseerrService @Inject constructor(private val restClient: JellyseerrRestClient) :
  JellyseerrService {

  override suspend fun signInWithJellyfin(
    jellyfinLogin: JellyfinLogin,
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

  override suspend fun logout(address: String): Flow<Unit> = flow {
    val url = "$address/api/v1/auth/logout"

    restClient.post<Unit, Unit>(url = url, body = Unit)

    emit(Unit)
  }
}
