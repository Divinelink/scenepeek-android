package com.divinelink.core.network.session.service

import com.divinelink.core.network.Routes
import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.session.model.AccessTokenBodyRequest
import com.divinelink.core.network.session.model.AccountDetailsResponseApi
import com.divinelink.core.network.session.model.CreateRequestTokenResponseApi
import com.divinelink.core.network.session.model.CreateSessionResponseApi
import com.divinelink.core.network.session.model.DeleteSessionResponseApi
import com.divinelink.core.network.session.model.v4.CreateAccessTokenRequest
import com.divinelink.core.network.session.model.v4.CreateAccessTokenResponse
import com.divinelink.core.network.session.model.v4.CreateRequestTokenRequest
import com.divinelink.core.network.session.util.buildAccessTokenUrl
import com.divinelink.core.network.session.util.buildCreateRequestTokenUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdSessionService(
  private val restClient: TMDbClient,
  private val authTMDBClient: AuthTMDbClient,
) : SessionService {

  override suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi> {
    val url = buildCreateRequestTokenUrl()

    val response = restClient.post<CreateRequestTokenRequest, CreateRequestTokenResponseApi>(
      body = CreateRequestTokenRequest(
        redirectTo = Routes.TMDb.AUTH_REDIRECT_URL,
      ),
      url = url,
    )
    return Result.success(response)
  }

  override suspend fun createAccessToken(requestToken: String): Result<CreateAccessTokenResponse> {
    val url = buildAccessTokenUrl()

    val response = restClient.post<CreateAccessTokenRequest, CreateAccessTokenResponse>(
      body = CreateAccessTokenRequest(
        requestToken = requestToken,
      ),
      url = url,
    )
    return Result.success(response)
  }

  override suspend fun createSession(accessToken: String): Result<CreateSessionResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/session/convert/4"

    val response = restClient.post<AccessTokenBodyRequest, CreateSessionResponseApi>(
      url = url,
      body = AccessTokenBodyRequest(accessToken),
    )

    return Result.success(response)
  }

  override suspend fun logout(accessToken: String): Result<DeleteSessionResponseApi> {
    val url = buildAccessTokenUrl()

    return runCatching {
      authTMDBClient.delete<AccessTokenBodyRequest, DeleteSessionResponseApi>(
        url = url,
        body = AccessTokenBodyRequest(accessToken),
      )
    }
  }

  override fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi> = flow {
    val url = "${authTMDBClient.tmdbUrl}/account"

    val response = authTMDBClient.get<AccountDetailsResponseApi>(url = url)

    emit(response)
  }
}
