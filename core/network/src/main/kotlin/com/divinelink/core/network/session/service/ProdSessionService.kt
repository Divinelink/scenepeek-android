package com.divinelink.core.network.session.service

import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.session.model.AccountDetailsResponseApi
import com.divinelink.core.network.session.model.CreateRequestTokenResponseApi
import com.divinelink.core.network.session.model.CreateSessionRequestApi
import com.divinelink.core.network.session.model.CreateSessionResponseApi
import com.divinelink.core.network.session.model.DeleteSessionRequestApi
import com.divinelink.core.network.session.model.DeleteSessionResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdSessionService(private val restClient: TMDbClient) : SessionService {

  override suspend fun createRequestToken(): Result<CreateRequestTokenResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/token/new"

    val response = restClient.get<CreateRequestTokenResponseApi>(url)
    return Result.success(response)
  }

  override suspend fun createSession(
    requestToken: CreateSessionRequestApi,
  ): Result<CreateSessionResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/session/new"

    val response = restClient.post<CreateSessionRequestApi, CreateSessionResponseApi>(
      url = url,
      body = requestToken,
    )

    return Result.success(response)
  }

  override suspend fun deleteSession(sessionId: String): Result<DeleteSessionResponseApi> {
    val url = "${restClient.tmdbUrl}/authentication/session"

    val response = restClient.delete<DeleteSessionRequestApi, DeleteSessionResponseApi>(
      url = url,
      body = DeleteSessionRequestApi(sessionId),
    )

    return Result.success(response)
  }

  override fun getAccountDetails(sessionId: String): Flow<AccountDetailsResponseApi> = flow {
    val url = "${restClient.tmdbUrl}/account?session_id=$sessionId"

    val response = restClient.get<AccountDetailsResponseApi>(url = url)

    emit(response)
  }
}
